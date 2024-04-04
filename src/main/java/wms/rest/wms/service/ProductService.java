package wms.rest.wms.service;

import jakarta.transaction.Transactional;
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
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product){
        return this.productRepository.save(product);
    }

    public List<Product> getProducts(){
        return this.productRepository.findAll();
    }

    public List<Product> findByNameContaining(String name){
        return this.productRepository.findByNameContaining(name);
    }

    public Optional<Product> findByGtin(int gtin){
        return this.productRepository.findByGtin(gtin);
    }

    public boolean existsById(int productId){
        return this.productRepository.existsById(productId);
    }

    public void delete(Product product){
        this.productRepository.delete(product);
    }

    public void deleteById(int productId) {
        this.productRepository.deleteById(productId);
    }

    public Optional<Product> findById(int productId){
        return this.productRepository.findById(productId);
    }
}

