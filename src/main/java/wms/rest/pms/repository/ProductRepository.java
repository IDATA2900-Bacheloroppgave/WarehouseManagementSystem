package wms.rest.pms.repository;

import org.springframework.data.repository.CrudRepository;
import wms.rest.pms.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
