package wms.rest.wms.api.controller;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.model.Trip;
import wms.rest.wms.service.TripService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public List<Trip> getTrips(){
        return this.tripService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Trip>> getTripById(@PathVariable("id") int id){
        Optional<Trip> trip = this.tripService.findTripById(id);
        ResponseEntity response;

        if(trip.isPresent()){
            response = new ResponseEntity(trip, HttpStatus.OK);
        } else {
            response = new ResponseEntity(trip, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTripById(@PathVariable("id") int tripId){
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
