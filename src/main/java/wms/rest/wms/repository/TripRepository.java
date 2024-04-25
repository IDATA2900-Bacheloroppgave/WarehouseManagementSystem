package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Shipment;
import wms.rest.wms.model.Trip;

import java.util.Optional;

/**
 * Repository interface for managing Trip entities in the database.
 * Provides CRUD operations and custom queries for retrieving Trip entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public interface TripRepository extends ListCrudRepository<Trip, Integer> {
}
