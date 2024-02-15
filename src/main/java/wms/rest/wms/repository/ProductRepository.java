package wms.rest.wms.repository;

import org.springframework.data.repository.CrudRepository;
import wms.rest.wms.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
