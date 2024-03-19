package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Product;

import java.util.Optional;

public interface ProductRepository extends ListCrudRepository<Product, Integer> {

    /**
     * Find specific product by name search.
     *
     * where product name like 'name'
     *
     * @param name name of the product to search for.
     * @return optional container on the found product.
     */
    Optional<Product> findByName(String name);

    /**
     * Find specific product by global trade identification number search.
     *
     * where product gtin like 'gtin'
     *
     * @param gtin global trade identification number of item to search for.
     * @return optional container on the found product.
     */
    Optional<Product> findByGtin(int gtin);






}