package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Packaging;

public interface PackagingRepository extends ListCrudRepository<Packaging, Integer> {
}
