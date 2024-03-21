package wms.rest.wms.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
