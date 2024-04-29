package wms.rest.wms.unit.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.CustomerRepository;
import wms.rest.wms.repository.StoreRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CustomerController using MockMvc.
 * Class tests API endpoints related to Customer operations.
 * Sets up the test environment by utilizing the H2 embedded database using @ActiveProfiles.
 *
 * @author Mikkel Stvelie.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerIntegrationTest {

    /** Provides support for Spring MVC testing. Allows to send HTTP requests into the DispatcherServlet and make assertions about the result */
    @Autowired
    private MockMvc mockMvc;

    /** Autowired CustomerRepository for interaction with the Customer H2 embedded database */
    @Autowired
    private CustomerRepository customerRepository;

    /** Autowired CustomerRepository for interaction with the Store H2 embedded database */
    @Autowired
    private StoreRepository storeRepository;

    /**
     * Tests the retrieval of a Customer by customerId from the API.
     *
     * @throws Exception if the perform request or expect actions fail.
     */
    @Test
    public void testGetCustomerById() throws Exception {
        // Arrange
        Store store = new Store();
        store.setStoreId(1);
        store.setName("Test store");
        store.setAddress("Test address");
        store.setCountry("Test ocuntry");
        store.setCity("Test City");
        store.setPostalCode(5004);
        store = storeRepository.save(store);

        Customer customer = new Customer(1, "test@example.com", "John", "Doe", "secretpassword", store);
        customer = customerRepository.save(customer);

        // Act and Assert
        mockMvc.perform(get("/api/customers/{customerId}", customer.getCustomerId()))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(customer.getCustomerId()))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
}
