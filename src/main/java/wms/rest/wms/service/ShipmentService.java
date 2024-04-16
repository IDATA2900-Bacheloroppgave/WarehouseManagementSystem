package wms.rest.wms.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.*;

import java.time.LocalDate;
import java.util.*;

@Service
public class ShipmentService {

    private ShipmentRepository shipmentRepository;

    private OrderService orderService;

    private static final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    public ShipmentService(
            ShipmentRepository shipmentRepository
            , OrderService orderService) {
        this.shipmentRepository = shipmentRepository;
        this.orderService = orderService;
    }

    /**
     * Returns a list of all shipments
     *
     * @return returns a list of all shipments.
     */
    public List<Shipment> getShipments(){
        return this.shipmentRepository.findAll();
    }

    /**
     * When a new Order is created by the client, it sets the OrderStatus to REGISTERED. Method finds all
     * laying orders with this status and adds them to a shipment.
     */
    @Transactional
    @Scheduled(initialDelay = 100000, fixedRate = 360000)
    public void createShipment() {
        try {
            // Group orders by both Store and LocalDate
            Map<AbstractMap.SimpleEntry<Store, LocalDate>, List<Order>> ordersByStoreDate = this.orderService.groupByStoreAndDeliveryDate();

            if (!ordersByStoreDate.isEmpty()) {
                for (Map.Entry<AbstractMap.SimpleEntry<Store, LocalDate>, List<Order>> entry : ordersByStoreDate.entrySet()) {
                    Store store = entry.getKey().getKey();
                    LocalDate deliveryDate = entry.getKey().getValue();
                    List<Order> orders = entry.getValue();

                    // Create a new shipment for each store and delivery date if there are registered orders
                    Shipment shipment = new Shipment();
                    shipment.setShipmentLoadLocation("Trondheim");
                    shipment.setShipmentUnloadLocation(store.getName()); // Set the unload location to the store's city
                    shipment = shipmentRepository.save(shipment); // Generate ID for logger

                    log.info("Shipment created for store {} for delivery date {}. Shipment ID: {}", store.getName(), deliveryDate, shipment.getShipmentId());

                    // Associate orders with the new shipment
                    for (Order order : orders) {
                        this.orderService.updateFromRegisteredToPicking(order);
                        order.setShipment(shipment);
                        shipment.getOrders().add(order);
                        log.info("Associating Order ID: {} with Shipment ID: {}", order.getOrderId(), shipment.getShipmentId());

                        for (OrderQuantities quantity : order.getQuantities()) {
                            Product product = quantity.getProduct();
                            Inventory inventory = product.getInventory();

                            // Update inventory when order is PICKING
                            inventory.setReservedStock(inventory.getReservedStock() - quantity.getProductQuantity());
                            inventory.setTotalStock(inventory.getTotalStock() - quantity.getProductQuantity());
                            inventory.setAvailableStock(inventory.getTotalStock());
                        }
                    }
                }
            } else {
                log.info("No registered orders found to add to a new shipment.");
            }
        } catch (Exception e) {
            log.error("An error occurred while creating shipments: {}", e.getMessage(), e);
        }
    }

    /**
     * Retrieve all shipments and sort them by end location (store)
     */
    public void findAndSortAllShipments() {
        List<Shipment> shipments = this.shipmentRepository.findAll();

    }


    @Transactional
    //@Scheduled(initialDelay = 90000, fixedRate = 360000)
    public void updateShipmentOrdersToPicked() {
        List<Shipment> shipments = this.shipmentRepository.findAll();
        for (Shipment shipment : shipments) {
            shipment.getOrders().stream().filter(order -> order.getOrderStatus() == OrderStatus.PICKING).forEach(order ->
                    order.setOrderStatus(OrderStatus.PICKED));
            this.shipmentRepository.save(shipment);
        }
    }
}
