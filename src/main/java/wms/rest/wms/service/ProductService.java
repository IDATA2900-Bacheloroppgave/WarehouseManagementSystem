package wms.rest.wms.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wms.rest.wms.exception.ProductDoesNotExistException;
import wms.rest.wms.model.Product;
import wms.rest.wms.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class for product api controller.
 */
@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    /**
     * Saves a Product to the repository
     *
     * @param product the product to save to the repository
     * @return if the Product already exists in the database, update the product based
     *         on the parameter input. Otherwise create a new entity
     */
    public Product saveProduct(Product product){
        return this.productRepository.save(product);
    }

    /**
     * Returns a List of all Products
     *
     * @return a list of all Products
     */
    public List<Product> getProducts(){
        return this.productRepository.findAll();
    }

    /**
     * Find a Product or several Products with the name search query
     *
     * @param name the name of the Product og Products to find
     * @return a list of Products that contains the name search query
     */
    public List<Product> findByNameContaining(String name){
        return this.productRepository.findByNameContaining(name);
    }

    /**
     * Find a Product by GTIN
     *
     * @param gtin the GTIN of the Product to find
     * @return Optional object of the Product, can be present or not
     */
    public Optional<Product> findByGtin(int gtin){
        return this.productRepository.findByGtin(gtin);
    }

    /**
     * Check if a Product exists by ID
     *
     * @param productId the ID of the Product to check if exists
     * @return true if the Product exists, false otherwise
     */
    public boolean existsById(int productId){
        return this.productRepository.existsById(productId);
    }

    /**
     * Delete a Product by ID
     *
     * @param productId the ID of the Product to delete
     */
    public void deleteById(int productId) {
        this.productRepository.deleteById(productId);
    }

    /**
     * Get an Optional object of Product
     *
     * @param productId the ID of the Product to get
     * @return an Optional object of Product. Can contain a Product or not
     */
    public Optional<Product> findById(int productId){
        return this.productRepository.findById(productId);
    }
}