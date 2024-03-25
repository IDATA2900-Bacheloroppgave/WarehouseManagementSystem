package wms.rest.wms.api.controller;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.model.Shipment;
import wms.rest.wms.service.ShipmentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<Optional<Shipment>> getShipments() {
        List<Shipment> shipments = this.shipmentService.getShipments();
        ResponseEntity response;

        if (!shipments.isEmpty()) {
            response = new ResponseEntity(shipments, HttpStatus.OK);
        } else {
            response = new ResponseEntity(shipments, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Method updates a Trip based on a Shipment, and all the OrderStatuses within a Shipment.
     * If every Order associated with the Shipment of the Trip has an OrderStatus of PICKED,
     * then we can update the TripStatus to READY_FOR_DEPARTURE.
     *
     * @param shipmentId
     * @return
     */
    @PutMapping("/{id}/update-status")
    public ResponseEntity<?> updateShipment(@PathVariable("id") int shipmentId){
        try{
            this.shipmentService.updateTripStatus(shipmentId);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating shipment");
        }
    }



}