package wms.rest.wms.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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

/**
 * Service class for Trip API controller
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Service
@AllArgsConstructor
public class TripService {

    /** Logger for this class used to log messages and errors,
     * @see LoggerFactory#getLogger(Class) */
    private static final Logger log = LoggerFactory.getLogger(TripService.class);

    /** Repository for handling Trip persistence operations */
    private TripRepository tripRepository;

    /** Repository for handling Shipment persistence operations */
    private ShipmentRepository shipmentRepository;

    /** Repository for handling Order persistence operations */
    private OrderRepository orderRepository;

    /**
     * Return a List of all Trips.
     *
     * @return a List of all Trips.
     */
    public List<Trip> getAll() {
        return this.tripRepository.findAll();
    }

    /**
     * Return an Optional object of Trip by tripId.
     *
     * @param id the tripId of the Trip to return.
     * @return an Optional object of the Trip, can be present or not.
     */
    public Optional<Trip> findTripById(int id) {
        return this.tripRepository.findById(id);
    }

    /**
     * Check if a Trip exists by tripId.
     *
     * @param tripId the tripId of the Trip to check if exists.
     * @return true if the Trip exists, false otherwise.
     */
    public boolean existsById(int tripId) {
        return this.tripRepository.existsById(tripId);
    }

    /**
     * Delete a Trip by tripId.
     *
     * @param tripId the tripId of the Trip to delete.
     */
    public void deleteById(int tripId) {
        this.tripRepository.deleteById(tripId);
    }

    /**
     * Creates a Trip and adds all Shipments with the same wishedDeliveryDate to the trip.
     * All orders inside a Shipment has to have the OrderStatus as PICKED.
     * Retrieves a random Driver from 'getRandomDriver' and assigns it to the trip.
     * This method is executed periodically according to a Scheduled configuration starting after an initial delay.
     */
    @Transactional
    @Scheduled(initialDelay = 120000, fixedRate = 300000)
    public void createTrip() {
        List<Shipment> shipments = this.shipmentRepository.findAll();
        Map<LocalDate, List<Shipment>> shipmentsByDeliveryDate = new HashMap<>();
        // Group shipments by wished delivery date if they are ready to be picked
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
                    shipment.setTrip(trip);
                    log.info("Shipment with ID: {} added to Trip for delivery date {}.", shipment.getShipmentId(), deliveryDate);
                    i++;
                }
                tripRepository.save(trip);
                log.info("Trip created with ID: {} for delivery date {} with {} shipments.", trip.getTripId(), deliveryDate, shipmentsForDate.size());
            }
        }
        if (shipmentsByDeliveryDate.isEmpty()) {
            log.info("No shipments were ready to be added to a trip based on the wished delivery date.");
        }
    }

    /**
     * Updates a Trip from TripStatus LOADING to DEPARTED.
     * This method is executed periodically according to a Scheduled configuration starting after an initial delay.
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
     * Updates an Trip from TripStatus DEPARTED to IN_TRANSIT. Also setting the
     * progressInPercent to 50.
     * This method is executed periodically according to a Scheduled configuration starting after an initial delay.
     * TODO: SEND PUSH NOTIFICATION
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
     * Return a random tripDriver from 'getTripDriverInformation'.
     *
     * @return a random tripDriver from 'getTripDriverInformation'.
     */
    private Map.Entry<String, Integer> getRandomDriver() {
        HashMap<String, Integer> driverInformation = getTripDriverInformation();
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(driverInformation.entrySet());
        Random random = new Random();
        return entries.get(random.nextInt(entries.size()));
    }

    /**
     * Create a HashMap with driver name and driver phone.
     *
     * @return a HashMap of driver name and driver phone.
     */
    private HashMap<String, Integer> getTripDriverInformation(){
        HashMap<String, Integer> driverInformation = new HashMap<>();
        driverInformation.put("Pietr Didrik", 48056693);
        driverInformation.put("Hans Pettersen", 49285943);
        driverInformation.put("Lus Hoston", 94682497);
        return driverInformation;
    }

    /**
     * Deliver a Shipment based of tripId. Method is mainly used for utility purposes
     * to specifically target each Shipment and deliver them instead of using a loop.
     *
     * @param tripId the tripId of the Trip to deliver the Shipments.
     * @return a String containing the information about the Shipment delivery.
     */
    @Transactional
    public String deliverNextShipment(int tripId) {
        Trip trip = tripRepository.findById(tripId).orElse(null);
        if (trip == null) {
            throw new IllegalArgumentException("Trip not found");
        }

        Set<Shipment> shipmentSet = trip.getShipments();
        // Convert the Set to a List and sort it (0,1,2,n)
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
     * Sets the OrderStatus of an Order inside a Shipment to DELIVERED. Also setting the
     * progressInPercent to 100.
     *
     * @param shipment the Shipment to update the OrderStatus and progressInPercent.
     */
    private void deliverShipment(Shipment shipment) {
        List<Order> orders = new ArrayList<>(shipment.getOrders());  // Make a copy of orders
        for (Order order : orders) {
            order.setProgressInPercent(100);
            order.setOrderStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
        }
        log.debug("Orders updated to DELIVERED status for shipment ID: {}", shipment.getShipmentId());
    }

    /**
     * Check if an Order inside a Shipment has already been delivered by checking if the
     * current OrderStatus of the Order is DELIVERED.
     *
     * @param shipment the Shipment to check if it has delivered Orders.
     * @return true if the Order is DELIVERED, false otherwise.
     */
    private boolean isDelivered(Shipment shipment) {
        return shipment.getOrders().stream().allMatch(order -> order.getOrderStatus() == OrderStatus.DELIVERED);
    }
}