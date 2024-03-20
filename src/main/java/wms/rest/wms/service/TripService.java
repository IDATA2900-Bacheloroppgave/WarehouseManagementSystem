package wms.rest.wms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Trip;
import wms.rest.wms.repository.TripRepository;

import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<Trip> findAll() {
        return this.tripRepository.findAll();
    }


}
