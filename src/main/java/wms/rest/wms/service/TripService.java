package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Shipment;
import wms.rest.wms.model.Trip;
import wms.rest.wms.repository.TripRepository;

import java.util.*;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
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
    public Trip updateCurrentLocation(int tripId){
        Trip trip = null;

        Optional<Trip> tripOptional = this.tripRepository.findByTripId(tripId);

        if (tripOptional.isPresent()) {
            trip = tripOptional.get();

            List<String> unloadLocations = findAllShipmentUnloadLocations(tripId);

            if (!unloadLocations.isEmpty()) {
                trip.setTripCurrentLocation(unloadLocations.get(0));
                trip = tripRepository.save(trip);
            }
        }
        return trip;
    }

    public String estimatedArrival(int tripId){
        Optional<Trip> tripOptional = this.tripRepository.findByTripId(tripId);

        String eta = null;

        if(tripOptional.isPresent()){
            Trip trip = tripOptional.get();
            String currentLocation = trip.getTripCurrentLocation();

            if(currentLocation.equals("Ålesund")) {
                eta = "2 Days";
            }
        }
        return eta;
    }

}
