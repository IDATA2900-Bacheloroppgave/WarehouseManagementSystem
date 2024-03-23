package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.exception.ShipmentNotFoundException;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.OrderStatus;
import wms.rest.wms.model.Shipment;
import wms.rest.wms.model.TripStatus;
import wms.rest.wms.repository.OrderRepository;
import wms.rest.wms.repository.ShipmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;


    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
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
    public void updateTripStatus(int shipmentId){

        Optional<Shipment> shipmentOptional = this.shipmentRepository.findById(shipmentId);

        if(shipmentOptional.isPresent()){
            Shipment shipment = shipmentOptional.get();

            boolean isPicked = shipment.getOrders().stream().allMatch(order
                    -> order.getOrderStatus() == OrderStatus.PICKED);
            if(isPicked){
                shipment.setTripStatus(TripStatus.READY_FOR_DEPARTURE);
                this.shipmentRepository.save(shipment);

            }
        }
    }

}
