package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Shipment;

/**
 * Repository interface for managing Order entities in the database.
 * Provides CRUD operations for retrieving Order entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public interface ShipmentRepository extends ListCrudRepository<Shipment, Integer> {
}
