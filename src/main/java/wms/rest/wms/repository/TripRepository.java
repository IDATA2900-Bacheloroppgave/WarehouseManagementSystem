package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Trip;

public interface TripRepository extends ListCrudRepository<Trip, Integer> {
}
