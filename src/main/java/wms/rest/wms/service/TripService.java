package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.OrderStatus;
import wms.rest.wms.model.Shipment;
import wms.rest.wms.model.Trip;
import wms.rest.wms.model.TripStatus;
import wms.rest.wms.repository.ShipmentRepository;
import wms.rest.wms.repository.TripRepository;

import java.util.*;

@Service
public class TripService {

    private TripRepository tripRepository;

    private ShipmentRepository shipmentRepository;

    public TripService(TripRepository tripRepository, ShipmentRepository shipmentRepository) {
        this.tripRepository = tripRepository;
        this.shipmentRepository = shipmentRepository;
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

    /**
     * Method finds all the unload locations associated with a Trip. A Trip can contain
     * several shipments with several different unload locations. Returns a list of every
     * location in Trip.
     *
     * @param tripId the tripId of the trip to iterate through all unload locations.
     * @return returns a list of all unload locations a trip has to go through.
     */
    public List<String> findAllShipmentUnloadLocations(int tripId) {
        Optional<Trip> tripOptional = this.tripRepository.findByTripId(tripId);
        List<String> unloadLocations = new ArrayList<>();

        if (tripOptional.isPresent()) {
            Trip trip = tripOptional.get();

            for (Shipment shipment : trip.getShipments()) {
                unloadLocations.add(shipment.getShipmentUnloadLocation());
            }
        } else {
            throw new EntityNotFoundException("Trip with ID: " + tripId + " was not found");
        }
        return unloadLocations;
    }

    /**
     * Method updates the current location of Trip. Currently the current location
     * is randomized by the list which is provided by 'findAllShipmentUnloadLocation'.
     *
     * @param tripId
     * @return
     */
    public Trip updateCurrentLocation(int tripId) {
        Trip trip = null;

        Optional<Trip> tripOptional = this.tripRepository.findByTripId(tripId);

        if (tripOptional.isPresent()) {
            trip = tripOptional.get();

            List<String> unloadLocations = findAllShipmentUnloadLocations(tripId);

            if (!unloadLocations.isEmpty()) {

                trip.setTripCurrentLocation(unloadLocations.get(0)); //TODO: CURRENLY SETS UNLOADLOCATION TO INDEX 0.
                trip = tripRepository.save(trip);
            }
        }
        return trip;
    }

    @Transactional
    @Scheduled(initialDelay = 300000, fixedRate = 600000)
    public void createTripWithScheduler() {
        List<Shipment> shipments = this.shipmentRepository.findAll();
        if (!shipments.isEmpty()) {
            for (Shipment shipment : shipments) {
                if (shipment.getTrip() == null) {
                    Trip trip = new Trip();
                    System.out.println("Trip with ID: " + trip.getTripId() + " was created.");
                    Map.Entry<String, Integer> randomDriver = getRandomDriver();
                    trip.setTripDriver(randomDriver.getKey());
                    trip.setTripDriverPhone(randomDriver.getValue());
                    trip.setTripStartDate(new Date(System.currentTimeMillis()));
                    trip.setTripStartLocation(shipment.getShipmentLoadLocation());
                    trip.setTripCurrentLocation(shipment.getShipmentUnloadLocation());
                    trip.setTripStatus(TripStatus.NOT_STARTED);

                    boolean isPicked = shipment.getOrders().stream().allMatch(order ->
                            order.getOrderStatus() == OrderStatus.PICKED);

                    if(isPicked){
                        shipment.setTrip(trip);
                        tripRepository.save(trip);
                    } else {
                        System.out.println("Orders inside shipment has not yet been picked.");
                    }
                }
            }
        } else {
            System.out.println("No shipments to add to trip.");
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
