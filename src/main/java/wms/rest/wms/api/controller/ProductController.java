package wms.rest.wms.api.controller;

import org.springframework.web.bind.annotation.*;
import wms.rest.wms.service.ProductService;

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
}
