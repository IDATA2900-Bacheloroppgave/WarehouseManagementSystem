package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Store;

/**
 * Repository interface for managing Store entities in the database.
 * Provides CRUD operations for retrieving Store entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public interface StoreRepository extends ListCrudRepository<Store, Integer> {
}
