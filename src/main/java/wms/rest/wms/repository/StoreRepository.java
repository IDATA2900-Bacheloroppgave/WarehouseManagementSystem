package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Store;

public interface StoreRepository extends ListCrudRepository<Store, Integer> {
}
