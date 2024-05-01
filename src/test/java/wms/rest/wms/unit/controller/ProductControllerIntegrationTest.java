package wms.rest.wms.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
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

import static org.hamcrest.Matchers.hasSize;
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
@Transactional
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() throws Exception {
        productRepository.deleteAll(); // Ensure clean state

        Product product1 = new Product();
        product1.setName("Product1");
        product1.setDescription("Description1");
        product1.setSupplier("Supplier1");
        product1.setBestBeforeDate(new Date());
        product1.setProductType(ProductType.DRY_GOODS);
        product1.setPrice(20.00);
        product1.setGtin(99999);
        product1.setBatch(11111);

        Product product2 = new Product();
        product2.setName("Product2");
        product2.setDescription("Description2");
        product2.setSupplier("Supplier2");
        product2.setBestBeforeDate(new Date());
        product2.setProductType(ProductType.DRY_GOODS);
        product2.setPrice(20.00);
        product2.setGtin(62312);
        product2.setBatch(62135);

        productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[1].name").value("Product2"));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = productRepository.findAll().get(0);
        mockMvc.perform(get("/api/products/{id}", product.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setDescription("New Description");
        newProduct.setSupplier("New Supplier");
        newProduct.setBestBeforeDate(new Date());
        newProduct.setProductType(ProductType.DRY_GOODS);
        newProduct.setPrice(25.00);
        newProduct.setGtin(54321);
        newProduct.setBatch(12345);

        String productJson = new ObjectMapper().writeValueAsString(newProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"));
    }
}