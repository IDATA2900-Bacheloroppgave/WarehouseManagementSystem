package wms.rest.wms.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.OrderRepository;
import wms.rest.wms.repository.ShipmentRepository;
import wms.rest.wms.repository.TripRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for Trip API controller
 */
@Service
public class TripService {

    private static final Logger log = LoggerFactory.getLogger(TripService.class);

    private TripRepository tripRepository;

    private ShipmentRepository shipmentRepository;

    private OrderRepository orderRepository;

    public TripService(TripRepository tripRepository, ShipmentRepository shipmentRepository, OrderRepository orderRepository) {
        this.tripRepository = tripRepository;
        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Returns a List of all Trips
     *
     * @return a list of all Trips
     */
    public List<Trip> getAll() {
        return this.tripRepository.findAll();
    }

    /**
     * Find a Trip by ID
     *
     * @param id the ID of the Trip to find
     * @return Optional object of the Trip, can be present or not
     */
    public Optional<Trip> findTripById(int id) {
        return this.tripRepository.findByTripId(id);
    }

    /**
     * Check if a Trip exists by ID
     *
     * @param tripId the ID of the Trip to check if exists
     * @return true if the Trip exists, false otherwise
     */
    public boolean existsById(int tripId) {
        return this.tripRepository.existsById(tripId);
    }

    /**
     * Delete a Trip by ID
     *
     * @param tripId the ID of the Trip to delete
     */
    public void deleteById(int tripId) {
        this.tripRepository.deleteById(tripId);
    }

    /**
     * Creates a Trip and adds all Shipments with the same wishedDeliveryDate. All orders inside a Shipment has
     * to have the OrderStatus as PICKED.
     */
    @Transactional
    @Scheduled(initialDelay = 120000, fixedRate = 300000)
    public void createTrip() {
        List<Shipment> shipments = this.shipmentRepository.findAll();
        Map<LocalDate, List<Shipment>> shipmentsByDeliveryDate = new HashMap<>();
        // Group shipments by wished delivery date if they are ready to be picked.
        for (Shipment shipment : shipments) {
            if (shipment.getTrip() == null && shipment.getOrders().stream()
                    .allMatch(order -> order.getOrderStatus() == OrderStatus.PICKED)) {
                LocalDate deliveryDate = shipment.getShipmentDeliveryDate();
                shipmentsByDeliveryDate.computeIfAbsent(deliveryDate, k -> new ArrayList<>()).add(shipment);
            }
        }
        // Create a trip for each delivery date with the corresponding shipments
        for (Map.Entry<LocalDate, List<Shipment>> entry : shipmentsByDeliveryDate.entrySet()) {
            LocalDate deliveryDate = entry.getKey();
            List<Shipment> shipmentsForDate = entry.getValue();
            if (!shipmentsForDate.isEmpty()) {
                int i = 0;
                Trip trip = new Trip();
                trip.setTripStartDate(LocalDate.now());
                trip.setTripStatus(TripStatus.LOADING);
                trip.setTripDriver(getRandomDriver().getKey());
                trip.setTripDriverPhone(getRandomDriver().getValue());
                trip.setTripStartLocation(shipmentsForDate.get(0).getShipmentLoadLocation());
                trip.setTripCurrentLocation(trip.getTripStartLocation());
                // Set other details for the trip as necessary
                for (Shipment shipment : shipmentsForDate) {
                    trip.getShipments().add(shipment);
                    shipment.setSequenceAtTrip(i);
                    shipment.setTrip(trip); // Ensure the bidirectional relationship is set
                    log.info("Shipment with ID: {} added to Trip for delivery date {}.", shipment.getShipmentId(), deliveryDate);
                    i++;
                }
                tripRepository.save(trip); // Save the trip with all its shipments
                log.info("Trip created with ID: {} for delivery date {} with {} shipments.", trip.getTripId(), deliveryDate, shipmentsForDate.size());
            }
        }
        if (shipmentsByDeliveryDate.isEmpty()) {
            log.info("No shipments were ready to be added to a trip based on the wished delivery date.");
        }
    }

    /**
     * Updates the TripStatus on a Trip from LOADING to DEPARTED
     * //TODO: SEND PUSH NOTIFICATION
     */
    @Transactional
    @Scheduled(initialDelay = 130000, fixedRate = 300000 )
    public void updateTripStatusFromLoadingToDeparted() {
        List<Trip> trips = this.tripRepository.findAll().stream().filter(trip ->
                trip.getTripStatus() == TripStatus.LOADING).toList();
        for(Trip trip : trips) {
            trip.setTripStatus(TripStatus.DEPARTED);
            this.tripRepository.save(trip);
        }
        log.info("Updated {} trips from LOADING to DEPARTED", trips.size());
    }

    /**
     * Updates the TripStatus on a Trip from DEPARTED to IN_TRANSIT
     * and update the progressInPercent to 50
     * //TODO: SEND PUSH NOTIFICATION
     */
    @Transactional
    @Scheduled(initialDelay = 150000, fixedRate = 300000 )
    public void updateTripStatusFromDepartedToInTransit() {
        List<Trip> trips = this.tripRepository.findAll().stream().filter(trip ->
                trip.getTripStatus() == TripStatus.DEPARTED).toList();
        for(Trip trip : trips) {
            trip.setTripStatus(TripStatus.IN_TRANSIT);
            for(Shipment shipment : trip.getShipments()) {
                for(Order order : shipment.getOrders()) {
                    order.setProgressInPercent(50);
                }
            }
            this.tripRepository.save(trip);
        }
        log.info("Updated {} trips from DEPARTED to IN TRANSIT", trips.size());
    }

    /**
     * Find a random driver from getTripDriverInformation() and return it
     *
     * @return a random driver from HashMap in getTripDriverInformation()
     */
    private Map.Entry<String, Integer> getRandomDriver() {
        HashMap<String, Integer> driverInformation = getTripDriverInformation();
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(driverInformation.entrySet());
        Random random = new Random();
        return entries.get(random.nextInt(entries.size()));
    }

    /**
     * Create a HashMap with drivers and add them into key-value pairs
     *
     * @return a HashMap of drivers
     */
    public HashMap<String, Integer> getTripDriverInformation(){
        HashMap<String, Integer> driverInformation = new HashMap<>();
        driverInformation.put("Pietr Didrik", 48056693);
        driverInformation.put("Hans Pettersen", 49285943);
        driverInformation.put("Lus Hoston", 94682497);
        return driverInformation;
    }

    /**
     * Deliver a Shipment based of TripId. Method is mainly used for utility purposes
     * to specifically target each shipment and deliver them instead of using a loop
     *
     * @param tripId the TripId of the Trip to deliver the Shipments
     * @return a String off the information of the Shipment
     */
    @Transactional
    public String deliverNextShipment(int tripId) {
        Trip trip = tripRepository.findById(tripId).orElse(null);
        if (trip == null) {
            throw new IllegalArgumentException("Trip not found");
        }

        Set<Shipment> shipmentSet = trip.getShipments();
        // Convert the Set to a List and sort it
        List<Shipment> sortedShipments = shipmentSet.stream()
                .sorted(Comparator.comparingInt(Shipment::getSequenceAtTrip))
                .toList();

        for (int i = 0; i < sortedShipments.size(); i++) {
            Shipment shipment = sortedShipments.get(i);
            if (!isDelivered(shipment)) {
                deliverShipment(shipment);
                log.info("Shipment with sequence {} has been delivered for trip ID: {}", shipment.getSequenceAtTrip(), tripId);

                // Set current location to the last delivered shipment's customer store name
                String currentLocation = shipment.getOrders().iterator().next().getCustomer().getStore().getCity();
                trip.setTripCurrentLocation(currentLocation);

                // Set next location if there is a next shipment
                if (i < sortedShipments.size() - 1) {
                    Shipment nextShipment = sortedShipments.get(i + 1);
                    String nextLocation = nextShipment.getOrders().iterator().next().getCustomer().getStore().getCity();
                    trip.setTripNextLocation(nextLocation);
                } else {
                    trip.setTripNextLocation("Trip finished");
                    trip.setTripStatus(TripStatus.FINISHED);
                    tripRepository.save(trip);
                    log.info("Trip ID: {} has been marked as FINISHED.", tripId);
                    return "Shipment with sequence " + shipment.getSequenceAtTrip() + " has been delivered, and the trip is now FINISHED.";
                }

                tripRepository.save(trip);
                return "Shipment with sequence " + shipment.getSequenceAtTrip() + " has been delivered.";
            }
        }

        return "All shipments were already delivered.";
    }


    /**
     * Delivers a Shipment by setting the OrderStatus to DELIVERED
     *
     * @param shipment the shipment to deliver its Orders
     */
    private void deliverShipment(Shipment shipment) {
        List<Order> orders = new ArrayList<>(shipment.getOrders());  // Make a copy of orders
        for (Order order : orders) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);  // Persist changes
        }
        log.debug("Orders updated to DELIVERED status for shipment ID: {}", shipment.getShipmentId());
    }


    /**
     * Check if an Order inside a Shipment has already been delivered by checking
     * for OrderStatus == DELIVERED
     *
     * @param shipment the Shipment to check if has DELIVERED orders
     * @return true if the Order is DELIVERED, false otherwise
     */
    private boolean isDelivered(Shipment shipment) {
        return shipment.getOrders().stream().allMatch(order -> order.getOrderStatus() == OrderStatus.DELIVERED);
    }
}