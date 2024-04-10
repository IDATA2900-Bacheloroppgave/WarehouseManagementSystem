package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.exception.ShipmentNotFoundException;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.*;

import java.util.*;

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
            , InventoryRepository inventoryRepository) {
        this.shipmentRepository = shipmentRepository;
        this.tripRepository = tripRepository;
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
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

    @Transactional
    public void updateInventoryForPickingOrders(Order order) {
        for (OrderQuantities quantity : order.getQuantities()) {
            Product product = quantity.getProduct();
            Inventory inventory = product.getInventory();
            inventory.setTotalStock(1000);
            this.inventoryRepository.save(inventory);
        }
    }


    //TODO: DOESNT WORK???????
    /**
     * When a new Order is created by the client, it sets the OrderStatus to REGISTERED. Method finds all
     * laying orders with this status and adds them to a shipment.
     */
    @Transactional
    @Scheduled(initialDelay = 180000, fixedRate = 12000)
    public void addRegisteredOrdersToShipmentWithScheduler() {
        Random random = new Random();
        List<String> randomUnloadLocations = new ArrayList<>(Arrays.asList("Kristiansund", "Molde", "Ålesund"));
        int randomIndex = random.nextInt(randomUnloadLocations.size());
        String randomLocation = randomUnloadLocations.get(randomIndex);

        List<Order> orders = this.orderRepository.findAll();
        if (!orders.isEmpty()) {
            Shipment shipment = new Shipment();
            shipment.setShipmentLoadLocation("Trondheim");
            shipment.setShipmentUnloadLocation(randomLocation);

            for (Order order : orders) {
                if (order.getOrderStatus() == OrderStatus.REGISTERED) {
                    order.setOrderStatus(OrderStatus.PICKING);

                    for(OrderQuantities quantity : order.getQuantities()) {
                        Product product = productRepository.findById(quantity.getProduct().getProductId())
                                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + quantity.getProduct().getProductId()));

                        Inventory inventory = product.getInventory();
                        inventory.setReservedStock(1000);
                        this.inventoryRepository.save(inventory);
                    }

                    order.setShipment(shipment);
                    shipment.getOrders().add(order);
                    orderRepository.save(order);
                    this.shipmentRepository.save(shipment);
                }
            }
        }
    }

    /**
     * Updates the OrderStatus from a Order from PICKING to PICKED.
     */
    @Scheduled(initialDelay = 120000, fixedRate = 240000) //TODO: NEEDS ADJUSTMENTS
   public void updatePickingOrdersWithScheduler(){
        List<Order> orders = this.orderRepository.findAll();
        if(!orders.isEmpty()){
            for(Order order : orders){
                if(order.getOrderStatus() == OrderStatus.PICKING){
                    order.setOrderStatus(OrderStatus.PICKED);
                    this.orderRepository.save(order);
                }
            }
        }
   }

    /**
     * Method assigns laying shipments into a new Trip.
     */
    @Transactional
    @Scheduled(fixedRate = 300000)
    public void updateTripStatusWithScheduler() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        List<Shipment> shipments = this.shipmentRepository.findAll();
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
    }
}
