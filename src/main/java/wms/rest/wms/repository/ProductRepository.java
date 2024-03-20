package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends ListCrudRepository<Product, Integer> {

    /**
     * Find a specific product or list based on search query.
     *
     * where product name contains 'name'
     *
     * If there are several products with the same pattern, for example Makrell
     * and Makrell i tomat, both the products will be returned as a list.
     *
     * @param name name of the product to search for.
     * @return list of the products found.
     */
    List<Product> findByNameContaining(String name);

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