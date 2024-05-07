package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import wms.rest.wms.model.OrderQuantities;

/**
 * Repository interface for managing OrderQuantities entities in the database.
 * Provides CRUD operations for retrieving OrderQuantities entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Repository
public interface OrderQuantitiesRepository extends ListCrudRepository<OrderQuantities, Integer> {
}
