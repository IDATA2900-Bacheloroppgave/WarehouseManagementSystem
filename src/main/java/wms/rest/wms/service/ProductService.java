package wms.rest.wms.service;

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

    public List<Product> getProducts(){
        return this.productRepository.findAll();
    }

    public List<Product> findByNameContaining(String name){
        return this.productRepository.findByNameContaining(name);
    }

    public Optional<Product> findByGtin(int gtin){
        return this.productRepository.findByGtin(gtin);
    }
}

