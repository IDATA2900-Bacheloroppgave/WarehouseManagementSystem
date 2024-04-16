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
    //@Scheduled(initialDelay = 300000, fixedRate = 600000)
    public void createTripWithScheduler() {
        List<Shipment> shipments = this.shipmentRepository.findAll();
        if (!shipments.isEmpty()) {
            for (Shipment shipment : shipments) {
                if (shipment.getTrip() == null) {
                    Trip trip = new Trip();
                    System.out.println("Trip with ID: " + trip.getTripId() + " was created.");
                    Map.Entry<String, Integer> randomDriver = getRandomDriver();
                    //shipment.setShipmentDeliveryDate(); //TODO: FIX
                    trip.setTripDriver(randomDriver.getKey());
                    trip.setTripDriverPhone(randomDriver.getValue());
                    trip.setTripStartDate(new Date(System.currentTimeMillis())); //TODO: USE LOCALDATE- NOT DATE
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
