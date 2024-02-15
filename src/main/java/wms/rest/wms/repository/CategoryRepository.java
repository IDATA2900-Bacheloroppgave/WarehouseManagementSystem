package wms.rest.wms.repository;

import org.springframework.data.repository.CrudRepository;
import wms.rest.wms.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
