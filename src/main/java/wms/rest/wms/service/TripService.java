package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.OrderStatus;
import wms.rest.wms.model.Shipment;
import wms.rest.wms.model.Trip;
import wms.rest.wms.model.TripStatus;
import wms.rest.wms.repository.ShipmentRepository;
import wms.rest.wms.repository.TripRepository;

import java.time.LocalDate;
import java.util.*;

@Service
public class TripService {

    private TripRepository tripRepository;

    private ShipmentRepository shipmentRepository;

    private ShipmentService shipmentService;

    public TripService(TripRepository tripRepository, ShipmentRepository shipmentRepository, ShipmentService shipmentService) {
        this.tripRepository = tripRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentService = shipmentService;
    }

    public List<Trip> findAll() {
        return this.tripRepository.findAll();
    }

    public Optional<Trip> findTripById(int id) {
        return this.tripRepository.findByTripId(id);
    }

    public boolean existsById(int tripId) {
        return this.tripRepository.existsById(tripId);
    }

    public void deleteById(int tripId) {
        this.tripRepository.deleteById(tripId);
    }

    @Transactional
    @Scheduled(initialDelay = 120000, fixedDelay = 300000)
    public void createTripsByWishedDeliveryDate() {
        Logger log = LoggerFactory.getLogger(this.getClass());
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
     */
    public void updateTripStatusFromLoadingToDeparted() {
        List<Trip> trips = this.tripRepository.findAll();
        for (Trip trip : trips) {
            if (trip.getTripStatus() == TripStatus.LOADING) {
                trip.setTripStatus(TripStatus.DEPARTED);
            }
        }
    }

    private Map.Entry<String, Integer> getRandomDriver() {
        HashMap<String, Integer> driverInformation = getTripDriverInformation();
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(driverInformation.entrySet());
        Random random = new Random();
        return entries.get(random.nextInt(entries.size()));
    }

    public HashMap<String, Integer> getTripDriverInformation(){
        HashMap<String, Integer> driverInformation = new HashMap<>();
        driverInformation.put("Pietr Didrik", 48056693);
        driverInformation.put("Hans Pettersen", 49285943);
        driverInformation.put("Lus Hoston", 94682497);
        return driverInformation;
    }
}
