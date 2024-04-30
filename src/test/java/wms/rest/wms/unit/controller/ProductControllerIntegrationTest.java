package wms.rest.wms.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wms.rest.wms.api.controller.ProductController;
import wms.rest.wms.model.Product;
import wms.rest.wms.model.ProductType;
import wms.rest.wms.repository.ProductRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ProductController using MockMvc.
 * Class tests API endpoints related to Product operations.
 * Sets up the test environment by utilizing the H2 embedded database using @ActiveProfiles.
 * Verifies that the request parameters are correctly mapped and if the response is as expected.
 *
 * @author Mikkel Stavelie.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {

    /** Provides support for Spring MVC testing. Allows to send HTTP requests into the DispatcherServlet and make assertions about the result */
    @Autowired
    private MockMvc mockMvc;

    /** Autowired ProductController for interaction with the Product H2 embedded database */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Set up test environment by initializing the database with fixed products.
     *
     * @throws Exception ParseException if there is an error in parsing the fixed date String.
     */
    @BeforeEach // Run before each test method
    public void setup() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fixedDate = dateFormat.parse("2024-08-24");

        Product product1 = new Product(1, "Sample Product", "Sample Description", "Sample Supplier", fixedDate, ProductType.DRY_GOODS, 10.99, 54321, 78905, null, null);
        Product product2 = new Product(2, "Another Product", "Another Description", "Another Supplier", fixedDate, ProductType.FROZEN_GOODS, 15.99, 12345, 67890, null, null);

        productRepository.save(product1);
        productRepository.save(product2);
    }

    /**
     * Tests the GET /api/products endpoint and the correct HTTP response status code.
     *
     * @throws Exception if the perform request or expect actions fail.
     */
    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                // Assertions for product1
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Sample Product"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Sample Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].supplier").value("Sample Supplier"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productType").value("DRY_GOODS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(10.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gtin").value(54321))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].batch").value(78905))
                // Assertions for product2
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].productId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Another Product"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Another Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].supplier").value("Another Supplier"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].productType").value("FROZEN_GOODS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(15.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].gtin").value(12345))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].batch").value(67890));
    }

    /**
     * Tests the GET /api/products/{productId} endpoint and the correct HTTP response status code.
     *
     * @throws Exception if the perform request or expect actions fail.
     */
    @Test
    public void testGetProductById() throws Exception {
        int productId = 1; // Added in @BeforeEach
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sample Product"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Sample Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplier").value("Sample Supplier"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productType").value("DRY_GOODS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gtin").value(54321))
                .andExpect(MockMvcResultMatchers.jsonPath("$.batch").value(78905));
    }

    /**
     * Tests the POST /api/products by adding a new Product to the List and the correct HTTP response status code.
     *
     * @throws Exception if the perform request or expect actions fail.
     */
    @Test
    public void testCreateProduct() throws Exception {
        // Create a product object to send with POST request
        Product newProduct = new Product(0, "New Product", "New Description", "New Supplier",
                new SimpleDateFormat("yyyy-MM-dd").parse("2024-09-01"),
                ProductType.DRY_GOODS, 25.00, 98765, 12345, null, null);
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(newProduct);
        System.out.println(productJson);

        // Simulate POST request to create a product
        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.supplier").value("New Supplier"))
                .andExpect(jsonPath("$.price").value(25.00));

        // Test handling of invalid data such as empty name
        Product invalidProduct = new Product(0, "", "Some Description", "Some Supplier",
                new SimpleDateFormat("yyyy-MM-dd").parse("2024-09-01"),
                ProductType.DRY_GOODS, 25.00, 98765, 12345, null, null);
        String invalidProductJson = objectMapper.writeValueAsString(invalidProduct);

        // Simulate POST request with invalid data
        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest());
    }
}
