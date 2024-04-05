package wms.rest.wms.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.model.Product;
import wms.rest.wms.model.Shipment;
import wms.rest.wms.service.ShipmentService;

import java.util.List;
import java.util.Optional;

@Tag(name = "Shipments", description = "All endpoint operations related to Shipments")
@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @Operation(summary = "Get a list of all shipments", description = "Returns a list of all shipments", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "204", description = "No content"),})
    @GetMapping
    public ResponseEntity<Optional<Shipment>> getShipments() {
        ResponseEntity response;
        List<Shipment> shipments = this.shipmentService.getShipments();
        if (!shipments.isEmpty()) {
            response = new ResponseEntity(shipments, HttpStatus.OK);
        } else {
            response = new ResponseEntity("There is no shipments available", HttpStatus.NO_CONTENT);
        }
        return response;
    }

    //TODO: ENDPOINT MIGHT NOT BE NECCESSARY, BUT KEEP UNTIL NOW.
    @Operation(summary = "Updates the status of a Trip", description = "Updates the Trip if from NOT_STARTED to READY_FOR_DEPARTURE" +
            " if all orders inside shipments have the order status of PICKED", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),})
    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateShipment(@PathVariable("id") int shipmentId){
        ResponseEntity response;
        try{
            this.shipmentService.updateTripStatus(shipmentId);
            response = new ResponseEntity("Successfull update", HttpStatus.OK);
        } catch(Exception e){
            response = new ResponseEntity("Couldnt not update shipment with shipmentId: " + shipmentId, HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}