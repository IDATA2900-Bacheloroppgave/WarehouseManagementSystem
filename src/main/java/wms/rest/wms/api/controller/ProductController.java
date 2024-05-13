package wms.rest.wms.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.model.Product;
import wms.rest.wms.service.ProductService;

import java.util.List;
import java.util.Optional;

/**
 * Controller class containing all endpoints related to Products.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Tag(name = "Products", description = "All endpoint operations related to Products")
@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    /** Service for handling Product service operations */
    private final ProductService productService;

    @Operation(summary = "Get a list of all products", description = "Returns a list of all products in database", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "204", description = "Bad Request"),})
    @GetMapping
    public ResponseEntity<String> getProducts() {
        ResponseEntity response;
        List<Product> products = this.productService.getProducts();
        if(!products.isEmpty()){
            response = new ResponseEntity(products, HttpStatus.OK);
        } else {
            response =  new ResponseEntity("There is no products available", HttpStatus.NO_CONTENT);
        }
        return response;
    }

    @Operation(summary = "Get a specific product by id", description = "Returns a specific product by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),})
    @GetMapping("{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable("id") int id) { //TODO: VALIDATE Pathvariable ?? Nødvendig?
        ResponseEntity response;
        Optional<Product> product = this.productService.findById(id);
        if(product.isPresent()){
            response = new ResponseEntity(product, HttpStatus.OK);
        } else {
            response = new ResponseEntity("Product with ID: " + id + " does not exist", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Operation(summary = "Search for a product by name query", description = "Returns a list of products containing search query", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),})
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Product>> findProductByName(@PathVariable("name") String name) {
        ResponseEntity response;
        List<Product> products = this.productService.findByNameContaining(name);
        if(!products.isEmpty()){
            response = new ResponseEntity(products, HttpStatus.OK);
        } else {
            response = new ResponseEntity("No such product with search query: " + name + " exists", HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Operation(summary = "Search for a product by gtin query", description = "Returns a list of products containing search query", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),})
    @GetMapping("/search/gtin/{gtin}")
    public ResponseEntity<Optional<Product>> findProductByGtin(@PathVariable("gtin") int gtin){
        ResponseEntity response;
        Optional<Product> product = this.productService.findByGtin(gtin);
        if(product.isPresent()){
           response = new ResponseEntity(product, HttpStatus.OK);
        } else {
            response = new ResponseEntity("No such product with search query: " + gtin + " exists", HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Operation(summary = "Delete a product by id", description = "Delete a product by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful delete", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") int productId){
        ResponseEntity response;
        if(this.productService.existsById(productId)) {
            this.productService.deleteById(productId);
            response = new ResponseEntity("Product with productId: " + productId + " was deleted", HttpStatus.OK);
        } else {
            response = new ResponseEntity("Product was not deleted. The main reason could be that the product already exists inside customer_order_quantites table", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Operation(summary = "Create a new product", description = "Create a new product", responses = {
            @ApiResponse(responseCode = "201", description = "Successful creation", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),})
    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        ResponseEntity response;
        Product newProduct = this.productService.createProduct(product);
        if(newProduct != null) {
            response = new ResponseEntity(newProduct, HttpStatus.CREATED);
        } else {
            response = new ResponseEntity("Product could not be created", HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int productId, @RequestBody Product productDetails) throws Exception {
        ResponseEntity response;
        Product updatedProduct = productService.updateProduct(productId, productDetails);
        if (updatedProduct != null) {
            response = new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>("Product could not be updated", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

}