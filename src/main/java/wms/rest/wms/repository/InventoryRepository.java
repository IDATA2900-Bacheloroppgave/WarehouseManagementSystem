package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Inventory;

public interface InventoryRepository extends ListCrudRepository<Inventory, Integer> {
}
