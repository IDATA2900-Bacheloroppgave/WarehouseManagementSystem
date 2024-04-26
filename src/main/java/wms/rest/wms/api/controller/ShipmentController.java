package wms.rest.wms.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.model.Product;
import wms.rest.wms.model.Shipment;
import wms.rest.wms.service.ShipmentService;

import java.util.List;
import java.util.Optional;

/**
 * Controller class containing all endpoints related to Shipments.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Tag(name = "Shipments", description = "All endpoint operations related to Shipments")
@RestController
@AllArgsConstructor
@RequestMapping("/api/shipments")
public class ShipmentController {

    /** Service for handling Shipment service operations */
    private final ShipmentService shipmentService;

    @Operation(summary = "Get a list of all shipments", description = "Returns a list of all shipments", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Shipment.class))),
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
}