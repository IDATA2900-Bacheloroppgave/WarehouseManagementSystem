package wms.rest.wms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Product;
import wms.rest.wms.repository.ProductRepository;

import java.util.Optional;

/**
 * Service class for productrepository interface.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Constructor.
     *
     * @param productRepository productrepository.
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Returns a list of all products in repository.
     *
     * @return a list of all products in repository.
     */
    public Iterable<Product> getAll() {
        return this.productRepository.findAll();
    }

    /**
     * Returns a single product in repository by id.
     *
     * @param id the id of the product.
     * @return the product with the suggested id.
     */
    public Optional<Product> findByid(int id){
        return this.productRepository.findById(id);
    }

    /**
     * Validate if product is not null.
     *
     * @param product to validate.
     * @return true if valid, false if invalid.
     */
    public boolean validateProduct(Product product){
        return product != null;
    }

    /**
     * Add a product to the repository.
     *
     * @param product the product to add to the repository.
     * @return true if added, false if not added.
     */
    public boolean add(Product product){
        if(validateProduct(product)){
            this.productRepository.save(product);
            return true;
        }
        return false;
    }

    /**
     * Deletes a product from the repository.
     *
     * @param id the id of the product to delete.
     * @return true if deleted, false if not deleted.
     */
    public boolean deleteProduct(int id) {
        if (this.productRepository.findById(id).isPresent()) {
            this.productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update an existing product from the repository.
     *
     * @param product the product to update properties.
     * @return true if updated, false if not updated.
     */
    public boolean update(Product product){
        Optional<Product> existingProduct = productRepository.findById(product.getId());

        if(existingProduct.isPresent()){
            Product updatedProduct = existingProduct.get();
            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setItem(product.getItem());
            updatedProduct.setInventory(product.getInventory());
            updatedProduct.setGtin(product.getGtin());
            updatedProduct.setBatch(product.getBatch());

            updatedProduct.getCategories().clear();
            updatedProduct.getCategories().addAll(product.getCategories());

            productRepository.save(updatedProduct);
            return true;
        }
        return false;
    }
}
