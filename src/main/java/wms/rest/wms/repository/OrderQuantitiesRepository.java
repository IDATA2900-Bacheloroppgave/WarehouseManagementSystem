package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.OrderQuantities;
import wms.rest.wms.service.OrderQuantitiesService;

/**
 * Repository interface for managing OrderQuantities entities in the database.
 * Provides CRUD operations for retrieving OrderQuantities entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public interface OrderQuantitiesRepository extends ListCrudRepository<OrderQuantities, Integer> {
}
