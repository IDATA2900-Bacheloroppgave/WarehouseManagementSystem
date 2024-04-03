package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.exception.ShipmentNotFoundException;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.OrderRepository;
import wms.rest.wms.repository.ShipmentRepository;
import wms.rest.wms.repository.TripRepository;

import java.util.*;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private OrderRepository orderRepository;


    public ShipmentService(ShipmentRepository shipmentRepository
            , TripRepository tripRepository
            , OrderRepository orderRepository) {
        this.shipmentRepository = shipmentRepository;
        this.tripRepository = tripRepository;
        this.orderRepository = orderRepository;
    }

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
     * When a new Order is created by the client, it sets the OrderStatus to REGISTERD. Method finds all
     * laying orders with this status and adds them to a shipment.
     */
    @Scheduled(initialDelay = 1000, fixedRate = 12000) //TODO: NEEDS ADJUSTMENTS
    public void addRegisteredOrdersToShipmentWithScheduler(){

        Random random = new Random();
        List<String> randomUnloadLocations = new ArrayList<>(Arrays.asList("Kristiansund", "Molde", "Ålesund")); //Initialize random unload locations
        int randomIndex = random.nextInt(randomUnloadLocations.size()); // get a random index
        String randomLocation = randomUnloadLocations.get(randomIndex); // assign a string to the random location


        List<Order> orders = this.orderRepository.findAll();
        if(!orders.isEmpty()){

            Shipment shipment = new Shipment();
            shipment.setShipmentLoadLocation("Trondheim");
            shipment.setShipmentUnloadLocation(randomLocation); //set shipment unload location to the random string

            for(Order order : orders){
                if(order.getOrderStatus() == OrderStatus.REGISTERED){
                    order.setOrderStatus(OrderStatus.PICKING);
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
    @Scheduled(initialDelay = 12000, fixedRate = 240000) //TODO: NEEDS ADJUSTMENTS
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
