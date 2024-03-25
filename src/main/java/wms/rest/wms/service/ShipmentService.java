package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
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
        if (shipmentOptional.isPresent()) {
            Shipment shipment = shipmentOptional.get();
            Trip trip = shipment.getTrip();
            boolean isPicked = shipment.getOrders().stream()
                    .allMatch(order -> order.getOrderStatus() == OrderStatus.PICKED);
            if (isPicked) {
                trip.setTripStatus(TripStatus.READY_FOR_DEPARTURE);
                tripRepository.save(trip);
            }
        }
    }

}
