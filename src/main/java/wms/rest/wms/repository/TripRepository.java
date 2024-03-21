package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Trip;

import java.util.Optional;

public interface TripRepository extends ListCrudRepository<Trip, Integer> {
    Optional<Trip> findByTripId(int tripId);



}
