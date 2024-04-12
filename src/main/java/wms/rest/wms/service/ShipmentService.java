package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.exception.ShipmentNotFoundException;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public ShipmentService(ShipmentRepository shipmentRepository
            , TripRepository tripRepository
            , OrderRepository orderRepository
            , InventoryRepository inventoryRepository
            , ProductRepository productRepository) {
        this.shipmentRepository = shipmentRepository;
        this.tripRepository = tripRepository;
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
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
     * Updates the TripStatus based on the OrderStatus of all Orders inside
     * the shipment object. If all Orders inside a shipment has the OrderStatus
     * of PICKED, the TripStatus can be updated to READY_FOR_DEPARTURE.
     *
     * @param shipmentId
     */
    public void updateTripStatus(int shipmentId) {
        Optional<Shipment> shipmentOptional = shipmentRepository.findById(shipmentId);

        try {
            Shipment shipment = shipmentOptional.get();
            Trip trip = shipment.getTrip();
            boolean isPicked = shipment.getOrders().stream()
                    .allMatch(order -> order.getOrderStatus() == OrderStatus.PICKED);
            if (isPicked) {
                trip.setTripStatus(TripStatus.READY_FOR_DEPARTURE);
                tripRepository.save(trip);
            }
        } catch(Exception e){
        }
    }

    /**
     * When a new Order is created by the client, it sets the OrderStatus to REGISTERED. Method finds all
     * laying orders with this status and adds them to a shipment.
     */
    @Transactional
    @Scheduled(initialDelay = 60000, fixedRate = 12000)
    public void addRegisteredOrdersToShipmentWithScheduler() {
        // Generate a random unload location
        Random random = new Random();
        List<String> randomUnloadLocations = new ArrayList<>(Arrays.asList("Kristiansund", "Molde", "Ålesund"));
        int randomIndex = random.nextInt(randomUnloadLocations.size());
        String randomLocation = randomUnloadLocations.get(randomIndex);

        try {
            // Fetch all orders with status REGISTERED
            List<Order> orders = orderRepository.findAll().stream()
                    .filter(order -> order.getOrderStatus() == OrderStatus.REGISTERED)
                    .toList();

            if (!orders.isEmpty()) {
                // Create a new shipment if there are registered orders
                Shipment shipment = new Shipment();
                shipment.setShipmentLoadLocation("Trondheim");
                shipment.setShipmentUnloadLocation(randomLocation);

                // Associate orders with the new shipment
                for (Order order : orders) {
                    order.setOrderStatus(OrderStatus.PICKING);
                    order.setProgressInPercent(10);
                    System.out.println("Order with ID: " + order.getOrderId() + " was changed from REGISTERED to PICKING.");
                    order.setShipment(shipment);
                    shipment.getOrders().add(order);
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

    /**
     * Updates the OrderStatus from a Order from PICKING to PICKED.
     */
    @Scheduled(fixedRate = 60000) //TODO: NEEDS ADJUSTMENTS
    public void updatePickingOrdersWithScheduler(){
        List<Order> orders = this.orderRepository.findAll();
        if(!orders.isEmpty()){
            for(Order order : orders){
                if(order.getOrderStatus() == OrderStatus.PICKING){
                    order.setOrderStatus(OrderStatus.PICKED);
                    order.setProgressInPercent(20);
                    this.orderRepository.save(order);
                    System.out.println("Order with ID: " + order.getOrderId() + " was changed from PICKING to PICKED.");
                }
            }
        }
   }

    /**
     * Method assigns laying shipments into a new Trip.
     */
    @Transactional
    //@Scheduled(fixedRate = 300000)
    public void updateTripStatusWithScheduler() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        List<Shipment> shipments = this.shipmentRepository.findAll();
        if(!shipments.isEmpty()){
            for (Shipment shipment : shipments) {
                if (shipment.getTrip() == null) {
                    Trip trip = new Trip();
                    trip.setTripDriver("Didrik");
                    trip.setTripDriverPhone(48056693);
                    trip.setTripStartDate(date);
                    trip.setTripEndDate(date);
                    trip.setTripStartLocation("Trondheim");
                    trip.setTripEndLocation("Ålesund");
                    trip.setTripStatus(TripStatus.NOT_STARTED);
                    trip.setTripCurrentLocation("Trondheim");

                    boolean isPicked = shipment.getOrders().stream().allMatch(order ->
                            order.getOrderStatus() == OrderStatus.PICKED);

                    // A shipment needs 2 or more Orders to update its TripStatus
                    boolean hasMinimumOrders = shipment.getOrders().size() >= 2;

                    if (isPicked
                            && hasMinimumOrders
                            && trip.getTripStatus() != TripStatus.READY_FOR_DEPARTURE) {

                        shipment.setTrip(trip);
                        this.shipmentRepository.save(shipment);
                        trip.setTripStatus(TripStatus.READY_FOR_DEPARTURE);
                        tripRepository.save(trip);
                    }
                }
            }
        } else {
            System.out.println("No shipments");
        }

    }
}
