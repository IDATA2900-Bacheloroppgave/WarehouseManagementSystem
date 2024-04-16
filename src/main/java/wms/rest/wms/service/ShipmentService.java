package wms.rest.wms.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.*;

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
     * Utility method for generating a random unload location for shipments.
     *
     * @return a random location from list.
     */
    public String getRandomUnloadLocation(){
        Random random = new Random();
        List<String> randomUnloadLocations = new ArrayList<>(Arrays.asList("Kristiansund", "Molde", "Ålesund"));
        int randomIndex = random.nextInt(randomUnloadLocations.size());
        String randomLocation = randomUnloadLocations.get(randomIndex);
        return randomLocation;
    }

    /**
     * When a new Order is created by the client, it sets the OrderStatus to REGISTERED. Method finds all
     * laying orders with this status and adds them to a shipment.
     */
    @Transactional
    @Scheduled(initialDelay = 60000, fixedRate = 360000)
    public void createShipment() {
        try {
            Map<Store, List<Order>> ordersByStore = this.orderService.groupByStore();
            // [Store 1] -> [Order 1, Order 2]
            // [Store 2] -> [Order 3, Order 4]

            if (!ordersByStore.isEmpty()) {
                for (Map.Entry<Store, List<Order>> entry : ordersByStore.entrySet()) {
                    Store store = entry.getKey();
                    List<Order> orders = entry.getValue();

                    // Create a new shipment for each store if there are registered orders
                    Shipment shipment = new Shipment();
                    shipment.setShipmentLoadLocation("Trondheim");
                    shipment.setShipmentUnloadLocation(orders.get(0).getCustomer().getStore().getCity()); //All orders inside same shipment have same address, so this is fine
                    shipment = shipmentRepository.save(shipment); // Generate ID for logger

                    log.info("Shipment created for store {}. Shipment ID: {}", store.getName(), shipment.getShipmentId());

                    // Associate orders with the new shipment
                    for (Order order : orders) {
                        this.orderService.updateFromRegisteredToPicking(order);
                        order.setShipment(shipment);
                        shipment.getOrders().add(order);
                        // Log the association of the order with the new shipment ID
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
                    // The shipment is already saved above; if you need to update it again after changes, you could call save again.
                }
            } else {
                log.info("No registered orders found to add to a new shipment.");
            }
        } catch (Exception e) {
            log.error("An error occurred while creating shipments: {}", e.getMessage(), e);
        }
    }

}
