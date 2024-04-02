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

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private TripRepository tripRepository;


    public ShipmentService(ShipmentRepository shipmentRepository, TripRepository tripRepository) {
        this.shipmentRepository = shipmentRepository;
        this.tripRepository = tripRepository;
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
     * Automatically update the TripStatus with Spring @Scheduled annotation. This means we dont have to
     * send a Put request to the API, but rather Spring runs this method every 10 minutes. Mainly
     * a utility method.
     *
     * Updates the TripStatus from NOT_STARTED to READY_FOR_DEPARTURE. Requires all Orders on Shipments
     * aboard the Trip to have OrderStatus as PICKED.
     */
    @Transactional
    @Scheduled(initialDelay = 1000, fixedRate = 600000)
    public void updateTripStatusWithScheduler(){
        List<Shipment> shipments = this.shipmentRepository.findAll();

        for(Shipment shipment : shipments){

            try{

                Trip trip = shipment.getTrip();

                boolean isPicked = shipment.getOrders().stream().allMatch(order ->
                        order.getOrderStatus() == OrderStatus.PICKED);

                // A shipment needs 2 or more Orders to update its TripStatus
                boolean hasMinimumOrders = shipment.getOrders().size() >= 2;

                if(isPicked
                        && hasMinimumOrders
                        && trip.getTripStatus() != TripStatus.READY_FOR_DEPARTURE){
                    trip.setTripStatus(TripStatus.READY_FOR_DEPARTURE);
                    tripRepository.save(trip);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
