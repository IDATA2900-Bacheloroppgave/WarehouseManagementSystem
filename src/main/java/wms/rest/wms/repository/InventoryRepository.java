package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Inventory;

/**
 * Repository interface for managing Inventory entities in the database.
 * Provides CRUD operations for retrieving Inventory entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public interface InventoryRepository extends ListCrudRepository<Inventory, Integer> {
}
