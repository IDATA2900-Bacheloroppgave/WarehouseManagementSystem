package wms.rest.pms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wms.rest.pms.model.Product;
import wms.rest.pms.repository.ProductRepository;

import java.util.Optional;

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

    public boolean validateProduct(Product product){
        return product != null;
    }

    public boolean add(Product product){
        if(validateProduct(product)){
            this.productRepository.save(product);
            return true;
        }
        return false;
    }

    public boolean delete(int id){
        if(productRepository.findById(id).isPresent()){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean update(Product product){
        Optional<Product> existingProduct = productRepository.findById(product.getId());

        if(existingProduct.isPresent()){
            Product updatedProduct = existingProduct.get();
            updatedProduct.setName(product.getName());
        }

    }



}
