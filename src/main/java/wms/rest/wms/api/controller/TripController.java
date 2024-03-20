package wms.rest.wms.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wms.rest.wms.model.Trip;
import wms.rest.wms.service.TripService;

import java.util.List;

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
}
