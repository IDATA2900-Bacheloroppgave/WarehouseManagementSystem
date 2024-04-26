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
import wms.rest.wms.model.Trip;
import wms.rest.wms.service.TripService;

import java.util.List;
import java.util.Optional;

/**
 * Controller class containing all endpoints related to Trips.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Tag(name = "Trips", description = "All endpoint operations related to Trips")
@RestController
@AllArgsConstructor
@RequestMapping("/api/trips")
public class TripController {

    /** Service for handling Trip service operations */
    private final TripService tripService;

    @Operation(summary = "Get a list of all trips", description = "Returns a list of all trips", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Trip.class))),
            @ApiResponse(responseCode = "204", description = "No content"),})
    @GetMapping
    public ResponseEntity<List<Trip>> getTrips() {
        ResponseEntity response;
        List<Trip> trips = this.tripService.getAll();
        if (!trips.isEmpty()) {
            response = new ResponseEntity(trips, HttpStatus.OK);
        } else {
            response = new ResponseEntity("There is no trips available", HttpStatus.NO_CONTENT);
        }
        return response;
    }

    @Operation(summary = "Get a specific trip by id", description = "Returns a specific trip by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Trip.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),})
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Trip>> getTripById(@PathVariable("id") int id) {
        Optional<Trip> trip = this.tripService.findTripById(id);
        ResponseEntity response;
        if (trip.isPresent()) {
            response = new ResponseEntity(trip, HttpStatus.OK);
        } else {
            response = new ResponseEntity(trip, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Operation(summary = "Delete a specific trip by id", description = "Deletes a specific trip by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Trip.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTripById(@PathVariable("id") int tripId) {
        ResponseEntity response;
        if (this.tripService.existsById(tripId)) {
            this.tripService.deleteById(tripId);
            response = new ResponseEntity(tripId, HttpStatus.OK);
        } else {
            response = new ResponseEntity(tripId, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Operation(summary = "Deliver Shipments based of TripId", description = "Delivered Shipments sequentially based of TripId", responses = {
            @ApiResponse(responseCode = "200", description = "Successful delivery", content = @Content(schema = @Schema(implementation = Trip.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),})
    @GetMapping("/delivershipments/{tripId}")
    public ResponseEntity<String> deliverNextShipment(@PathVariable("tripId") int tripId) {
        ResponseEntity response;
        String delivered = this.tripService.deliverNextShipment(tripId);
        if (!delivered.isEmpty()) {
            response = new ResponseEntity(delivered, HttpStatus.OK);
        } else {
            response = new ResponseEntity("Could not find Trip with ID " + tripId, HttpStatus.NOT_FOUND);
        }
        return response;
    }
}