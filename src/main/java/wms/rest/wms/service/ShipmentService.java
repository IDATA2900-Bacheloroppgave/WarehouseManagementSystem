package wms.rest.wms.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Service class for Shipment API controller.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Service
@AllArgsConstructor
public class ShipmentService {

    /** Logger for this class used to log messages and errors,
    * @see LoggerFactory#getLogger(Class) */
    private static final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    /** Repository for handling Shipment persistence operations */
    private ShipmentRepository shipmentRepository;

    /** Service for handling Order persistence operations */
    private OrderService orderService;

    /**
     * Return a list of all Shipments
     *
     * @return return a List of all Shipments.
     */
    public List<Shipment> getShipments(){
        return this.shipmentRepository.findAll();
    }

    /**
     * Schedules and creates Shipments based on grouped Orders from different Stores by delivery date.
     * This method is executed periodically according to a Scheduled configuration starting after an initial delay.
     *
     * @see OrderService#groupByStoreAndDeliveryDate() for details on how Orders are grouped.
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

                    // Create a new Shipment for each Store and delivery date if there are registered Orders
                    Shipment shipment = new Shipment();
                    shipment.setShipmentLoadLocation("Trondheim");
                    shipment.setShipmentUnloadLocation(store.getName());
                    shipment.setShipmentDeliveryDate(deliveryDate);
                    shipment = shipmentRepository.save(shipment);
                    log.info("Shipment created for store {} for delivery date {}. Shipment ID: {}", store.getName(), deliveryDate, shipment.getShipmentId());

                    // Associate Orders with the new Shipment
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
     * Update all Orders inside a Shipment from OrderStatus PICKING to PICKED.
     * This method is executed periodically according to a Scheduled configuration starting after an initial delay.
     */
    @Transactional
    @Scheduled(initialDelay = 110000, fixedRate = 360000)
    public void updateShipmentOrdersToPicked() {
        List<Shipment> shipments = this.shipmentRepository.findAll();
        for (Shipment shipment : shipments) {
            List<Order> ordersToUpdate = shipment.getOrders().stream()
                    .filter(order -> order.getOrderStatus() == OrderStatus.PICKING)
                    .toList();
            for (Order order : ordersToUpdate) {
                this.orderService.updateFromPickingToPicked(order);
            }
            this.shipmentRepository.save(shipment);
        }
    }
}