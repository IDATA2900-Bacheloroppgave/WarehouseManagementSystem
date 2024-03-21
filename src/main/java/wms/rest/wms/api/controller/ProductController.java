package wms.rest.wms.api.controller;

import org.apache.coyote.Response;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.model.Product;
import wms.rest.wms.service.ProductService;

import java.util.List;
import java.util.Optional;

/**
 * Class represents the /products endpoint.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Returns a list of all products.
     *
     * @return a list of all products.
     *          Http status code 404 if list is empty.
     *          Http status code 200 if list is not empty.
     */
    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = this.productService.getProducts();
        ResponseEntity response;

        if(!products.isEmpty()){
            response = new ResponseEntity(products, HttpStatus.OK);
        } else {
            response = new ResponseEntity(products, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable("id") int id) {
        ResponseEntity response;

        Optional<Product> product = this.productService.findByid(id);
        if(product.isPresent()){
            response = new ResponseEntity(product, HttpStatus.OK);
        } else {
            response = new ResponseEntity(product, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Search for a product that contains 'name' query.
     *
     * @param name query of product to search for.
     * @return a list of products that contains query.
     *          Http status code 404 if no products found.
     *          Http status code 200 if product(s) are found.
     */
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Product>> findProductByName(@PathVariable("name") String name) {
        List<Product> products = this.productService.findByNameContaining(name);
        ResponseEntity response;

        if(!products.isEmpty()){
            response = new ResponseEntity(products, HttpStatus.OK);
        } else {
            response = new ResponseEntity(products, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Search for a product that has a specific global trade identification number.
     *
     * @param gtin query of gtin number of product to search for.
     * @return a specific product with queried gtin number.
     *          Http status code 404 if no associated product.
     *          Http status code 200 if product is found.
     *
     */
    @GetMapping("/search/gtin/{gtin}")
    public ResponseEntity<Optional<Product>> findProductByGtin(@PathVariable("gtin") int gtin){
        Optional<Product> product = this.productService.findByGtin(gtin);
        ResponseEntity response;

        if(product.isPresent()){
           response = new ResponseEntity(product, HttpStatus.OK);
        } else {
            response = new ResponseEntity(product, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Ikke testet
     * @param productId
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") int productId){
        ResponseEntity response;

        if(this.productService.existsByid(productId)){
            response = new ResponseEntity(productId, HttpStatus.OK);
            this.productService.deleteByid(productId);
        } else {
            response = new ResponseEntity(productId, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        ResponseEntity response;
        try{
            Product newProduct = this.productService.saveProduct(product);
            response = new ResponseEntity(newProduct, HttpStatus.OK);
        } catch (Exception e){
            response = new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
