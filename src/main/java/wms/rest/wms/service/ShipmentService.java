package wms.rest.wms.service;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.*;

import java.util.*;

@Service
public class ShipmentService {

    private ShipmentRepository shipmentRepository;

    private OrderService orderService;

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
            // Fetch REGISTERED orders
            List<Order> registeredOrders = this.orderService.getRegisteredOrders();
            if (!registeredOrders.isEmpty()) {
                // Create a new shipment if there are registered orders
                Shipment shipment = new Shipment();
                System.out.println("Shipment with ID: " + shipment.getShipmentId() + " was created.");
                shipment.setShipmentLoadLocation("Trondheim");
                shipment.setShipmentUnloadLocation(getRandomUnloadLocation());

                // Associate orders with the new shipment
                for (Order order : registeredOrders) {
                    this.orderService.updateFromRegisteredToPicking(order);
                    System.out.println("Order with ID: " + order.getOrderId() + " was changed from REGISTERED to PICKING.");
                    order.setShipment(shipment);
                    shipment.getOrders().add(order);

                    for(OrderQuantities quantity : order.getQuantities()){
                        Product product = quantity.getProduct();
                        Inventory inventory = product.getInventory();

                        // Update inventory when order is PICKING
                        inventory.setReservedStock(inventory.getReservedStock() - quantity.getProductQuantity());
                        inventory.setTotalStock(inventory.getTotalStock() - quantity.getProductQuantity());
                        inventory.setAvailableStock(inventory.getTotalStock());
                    }
                }

                // Save the shipment once after all orders are associated
                shipmentRepository.save(shipment);
                System.out.println("Shipment saved with ID: " + shipment.getShipmentId());
            } else {
                System.out.println("No registered orders found to add to a new shipment.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
