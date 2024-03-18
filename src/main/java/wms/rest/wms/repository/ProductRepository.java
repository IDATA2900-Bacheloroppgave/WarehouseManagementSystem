package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Product;

public interface ProductRepository extends ListCrudRepository<Product, Integer> {



}