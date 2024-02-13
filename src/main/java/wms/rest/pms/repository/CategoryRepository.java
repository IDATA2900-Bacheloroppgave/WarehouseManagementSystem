package wms.rest.pms.repository;

import org.springframework.data.repository.CrudRepository;
import wms.rest.pms.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
