package wms.rest.pms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.pms.model.Product;
import wms.rest.pms.service.ProductService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

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
