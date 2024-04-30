package wms.rest.wms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Product;
import wms.rest.wms.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class for product api controller.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Service
@AllArgsConstructor
public class ProductService {

    /** Repository for handling Product persistence operations */
    private ProductRepository productRepository;

    /**
     * Creates a Product and saves it to the repository.
     *
     * @param product the Product to create and save to the repository.
     * @return saves the Product to the repository. If the Product does not exist in the repository,
     * save and persist the new Product. If the incoming JSON payload of the Product already exist
     * in the repository, merge the new Product with the old.
     */
    public Product createProduct(Product product){
        return this.productRepository.save(product);
    }

    /**
     * Return a List of all Products.
     *
     * @return a List of all Products
     */
    public List<Product> getProducts(){
        return this.productRepository.findAll();
    }

    /**
     * Search the repository for a Product or Products containing the name search query.
     *
     * @param name the name of the Product or Products to get.
     * @return a List of Products that contains the name search query.
     */
    public List<Product> findByNameContaining(String name){
        return this.productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Search the repository for a Product containing the GTIN search query.
     *
     * @param gtin the GTIN of the Product to get.
     * @return an Optional object of the Product, can be present or not.
     */
    public Optional<Product> findByGtin(int gtin){
        return this.productRepository.findByGtin(gtin);
    }

    /**
     * Check if a Product exists by productId.
     *
     * @param productId the productId of the Product to check if exists.
     * @return true if the Product exists, false otherwise.
     */
    public boolean existsById(int productId){
        return this.productRepository.existsById(productId);
    }

    /**
     * Delete a Product by productId.
     *
     * @param productId the productId of the Product to delete.
     */
    public void deleteById(int productId) {
        this.productRepository.deleteById(productId);
    }

    /**
     * Return an Optional
     *
     * @param productId the productId of the Product to return.
     * @return an Optional object of Product, can be present or not.
     */
    public Optional<Product> findById(int productId){
        return this.productRepository.findById(productId);
    }
}