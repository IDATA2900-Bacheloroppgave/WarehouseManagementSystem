package wms.rest.wms.unit.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wms.rest.wms.api.controller.ProductController;
import wms.rest.wms.model.Product;
import wms.rest.wms.model.ProductType;
import wms.rest.wms.repository.ProductRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ProductController using MockMvc.
 * Class tests API endpoints related to Product operations.
 * Sets up the test environment by utilizing the H2 embedded database using @ActiveProfiles.
 *
 * @author Mikkel Stvelie.
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

    /** TODO: DOES NOT RETURN THE CORRECT RESPONSE
     * Tests the retrieval of all Products from the API.
     *
     * @throws Exception if hte perform request or expect actions fail.
     */
    @Test
    public void testGetAllProducts() throws Exception{
        Product product = new Product(1,
                "Sample Product", "Sample Description", "Sample Supplier",
                LocalDate.of(2024, 8, 24), ProductType.DRY_GOODS, 10.99,
                54321, 78905, null, null);

        Product product2 = new Product(2,
                "Sample Product", "Sample Description", "Sample Supplier",
                LocalDate.of(2024, 8, 24), ProductType.REFRIGERATED_GOODS, 12.0,
                12345, 51351, null, null);

        this.productRepository.save(product);
        this.productRepository.save(product2);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].name").value("Sample Product 1"))
                .andExpect(jsonPath("$[0].description").value("Sample Description 1"))
                .andExpect(jsonPath("$[0].supplier").value("Sample Supplier 1"))
                .andExpect(jsonPath("$[0].bestBeforeDate").value("2024-08-24"))
                .andExpect(jsonPath("$[0].productType").value("DRY_GOODS"))
                .andExpect(jsonPath("$[0].price").value(10.99))
                .andExpect(jsonPath("$[0].gtin").value(54321))
                .andExpect(jsonPath("$[0].batch").value(78905))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].name").value("Sample Product 2"))
                .andExpect(jsonPath("$[1].description").value("Sample Description 2"))
                .andExpect(jsonPath("$[1].supplier").value("Sample Supplier 2"))
                .andExpect(jsonPath("$[1].bestBeforeDate").value("2024-08-25"))
                .andExpect(jsonPath("$[1].productType").value("REFRIGERATED_GOODS"))
                .andExpect(jsonPath("$[1].price").value(12.0))
                .andExpect(jsonPath("$[1].gtin").value(12345))
                .andExpect(jsonPath("$[1].batch").value(51351));
    }
}
