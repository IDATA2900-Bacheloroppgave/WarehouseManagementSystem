package wms.rest.wms.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wms.rest.wms.api.model.LoginBody;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.CustomerRepository;
import wms.rest.wms.repository.StoreRepository;
import wms.rest.wms.service.security.EncryptionService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CustomerController using MockMvc.
 * Class tests API endpoints related to Customer operations.
 * Sets up the test environment by utilizing the H2 embedded database using @ActiveProfiles.
 * Verifies that the request parameters are correctly mapped and if the response is as expected.
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

    /** Autowired Objectmapper for object serialization and JSON deserialization */
    @Autowired
    private ObjectMapper objectMapper;

    /** Autowired EncryptionService for required password encryption in login */
    @Autowired
    private EncryptionService encryptionService;

    @BeforeEach
    public void setup() {
        Store store = new Store();
        store.setStoreId(1);
        store.setName("Test store");
        store.setAddress("Test address");
        store.setCountry("Test country");  // Fixed a typo here
        store.setCity("Test City");
        store.setPostalCode(5004);
        storeRepository.save(store);

        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setStore(store);

        // Encrypt the password to avoid IllegalArgumentException: Invalid salt
        String encryptedPassword = encryptionService.encryptPassword("secretpassword11");
        customer.setPassword(encryptedPassword);

        // Now save the customer with the encrypted password
        customerRepository.save(customer);
    }

    /**
     * Tests the retrieval of a Customer by customerId from the API and the correct return
     * HTTP response status code.
     *
     * @throws Exception if the perform request or expect actions fail.
     */
    @Test
    public void testGetCustomerById() throws Exception {
        int customerId = 1; // Know its 1 from the setup() method
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{customerId}", customerId))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(customerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"));
    }

    /**
     * Tests the Customer /auth/login endpoint and the correct HTTP response status code.
     * @see wms.rest.wms.api.controller.auth.AuthenticationController#loginUser(LoginBody).
     *
     * @throws Exception if the perform request or expect actions fail.
     */
    @Test
    public void testLoginSuccess() throws Exception {
        LoginBody validLogin = new LoginBody("test@example.com", "secretpassword11");
        String jsonRequest = objectMapper.writeValueAsString(validLogin);

        // Perform POST request and verify the response status is 200
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists());
    }

    /**
     * Test the Customer /auth/login endpoint and the correct return HTTP response status code.
     *
     * @throws Exception if the perform request or expect actions fail.
     */
    @Test
    public void testLoginFailure() throws Exception {
        // Incorrect credentials
        LoginBody invalidLogin = new LoginBody("testt@example.com", "secretpassword11");
        String jsonRequest = objectMapper.writeValueAsString(invalidLogin);

        // Perform POST request and verify the response status is 400
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
}
