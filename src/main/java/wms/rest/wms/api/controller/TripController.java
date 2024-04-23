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
import wms.rest.wms.model.Trip;
import wms.rest.wms.service.TripService;

import java.util.List;
import java.util.Optional;

@Tag(name = "Trips", description = "All endpoint operations related to Trips")
@RestController
@RequestMapping("/api/trips")
public class TripController {

    private TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(summary = "Get a list of all trips", description = "Returns a list of all trips", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
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
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
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
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),})
    @DeleteMapping("{id}")
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
}