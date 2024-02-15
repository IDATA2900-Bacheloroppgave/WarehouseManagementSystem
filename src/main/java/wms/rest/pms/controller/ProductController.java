package wms.rest.pms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.pms.model.Product;
import wms.rest.pms.service.ProductService;

/**
 * Class represents the /products endpoint.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    /**
     * Constructor.
     *
     * @param productService productservice.
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Post mapping for /products endpoint. Adds a product to the database.
     *
     * @param product the product to add to the database. Requires JSON body in http POST request.
     * @return responseentity based on the result of the post request.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product){
        boolean added = this.productService.add(product);
        try {
            if(added){
                return new ResponseEntity<>("Product was added", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product was not added", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete mapping for /products endpoint. Deletes a product from the database.
     *
     * @param id the id of the product to delete.
     * @return responsentity based on the result of the delete request.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id){
        boolean deleted = this.productService.deleteProduct(id);
        if(deleted){
            return new ResponseEntity<>("Product was deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product was not deleted", HttpStatus.BAD_REQUEST);
        }
    }
}