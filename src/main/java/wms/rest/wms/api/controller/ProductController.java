package wms.rest.wms.api.controller;

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
}
