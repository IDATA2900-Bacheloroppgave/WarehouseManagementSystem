package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import wms.rest.wms.model.Trip;

/**
 * Repository interface for managing Trip entities in the database.
 * Provides CRUD operations and custom queries for retrieving Trip entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Repository
public interface TripRepository extends ListCrudRepository<Trip, Integer> {
}
