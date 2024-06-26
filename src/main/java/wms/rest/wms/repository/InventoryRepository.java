package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import wms.rest.wms.model.Inventory;

/**
 * Repository interface for managing Inventory entities in the database.
 * Provides CRUD operations for retrieving Inventory entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Repository
public interface InventoryRepository extends ListCrudRepository<Inventory, Integer> {
}
