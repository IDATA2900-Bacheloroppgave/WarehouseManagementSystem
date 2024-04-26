package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Product entities in the database.
 * Provides CRUD operations and custom queries for retrieving Product entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public interface ProductRepository extends ListCrudRepository<Product, Integer> {

    /**
     * Retrieves a List of Products whose name contains the specified String.
     *
     * @param name the String to search for in the Product names.
     * @return a List of Products whose names contain the specified String.
     */
    List<Product> findByNameContaining(String name);

    /**
     * Retrieve a Product with the specified Global Trade Item Number (GTIN).
     *
     * @param gtin the Global Trade Item Number (GTIN) of the Product to retrieve.
     * @return an Optional object containing the Product with the specified GTIN, can be present or not.
     */
    Optional<Product> findByGtin(int gtin);
}