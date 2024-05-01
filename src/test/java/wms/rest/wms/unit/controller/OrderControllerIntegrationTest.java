package wms.rest.wms.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import wms.rest.wms.api.model.LoginBody;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.OrderStatus;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.CustomerRepository;
import wms.rest.wms.repository.OrderRepository;
import wms.rest.wms.repository.StoreRepository;
import wms.rest.wms.service.security.EncryptionService;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for OrderController using MockMvc.
 * Class tests API endpoints related to Order endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerIntegrationTest {

    /** Provides support for Spring MVC testing. Allows to send HTTP requests into the DispatcherServlet and make assertions about the result */
    @Autowired
    private MockMvc mockMvc;

    /** Autowired CustomerRepository for interaction with the H2 embedded database */
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    /** Autowired CustomerRepository for interaction with the H2 embedded database */
    @Autowired
    private StoreRepository storeRepository;

    /** Autowired Objectmapper for object serialization and JSON deserialization */
    @Autowired
    private ObjectMapper objectMapper;

    /** Autowired EncryptionService for required password encryption in login */
    @Autowired
    private EncryptionService encryptionService;

    /** Declare Customer at class level for easier accessibility */
    private Customer customer;

    /** Declare Store at class level for easier accessibility */
    private Store store;

    /** Declare Order at class level for easier accessibility */
    private Order order;

    /**
     * Prepare the test environment before each test method.
     * This method is run before each test method to ensure the testing environment is properly initialized.
     * Reduces boilerplate code and makes test more clear and concise.
     */
    @BeforeEach
    public void setup() {
        store = new Store();
        store.setStoreId(1);
        store.setName("Test store");
        store.setAddress("Test address");
        store.setCountry("Test country");
        store.setCity("Test City");
        store.setPostalCode(5004);
        storeRepository.save(store);

        customer = new Customer();
        customer.setEmail("testt@example.com");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setStore(store);

        // Encrypt the password
        String encryptedPassword = encryptionService.encryptPassword("secretpassword11");
        customer.setPassword(encryptedPassword);
        this.customerRepository.save(customer);

        order = new Order();
        order.setOrderDate(LocalDate.now());
            String dateString = "2024-08-24";
            LocalDate date = LocalDate.parse(dateString);
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setWishedDeliveryDate(date);
        order.setProgressInPercent(20);
        order.setCustomer(customer);
        order.setStore(store);
        this.orderRepository.save(order);
    }

    /**
     * Cleans up the embedded H2 database after each test.
     * This method ensures that all entities are deleted from the repository,
     * maintaining a clean state for subsequent tests.
     */
    @AfterEach
    public void cleanup() {
        this.orderRepository.deleteAll();
        this.customerRepository.deleteAll();
        this.storeRepository.deleteAll();
    }

    /**
     * Return a JWT token when the login request is performed and successful.
     *
     * @return a JWT token when the login request if performed and successful.
     * @throws Exception if the perform request of expect actions fail.
     */
    private String authenticateAndGetJwt() throws Exception {
        LoginBody validLogin = new LoginBody("testt@example.com", "secretpassword11");
        String jsonRequest = objectMapper.writeValueAsString(validLogin);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseString).path("jwt").asText();
    }

    /**
     * Tests the retrieval of Orders by an authenticated Customer from the API and return
     * the correct HTTP response status code.
     * Tests the full flow from when the Customer logs in, gets assigned a JWT, and
     * view the Orders.
     *
     * @throws Exception if the perform request or expect actions fail.
     */
    @Test
    public void testGetOrdersWithAuthentication() throws Exception {
        String jwt = authenticateAndGetJwt();
        mockMvc.perform(get("/api/orders")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderStatus").value("DELIVERED")); // Check random field that its the correct Order
    }

    /**
     * Tests the retrieval of a specific Order by orderId by an authenticated Customer from the API and return
     * the correct HTTP response status code.
     * Tests the full flow from when the Customner logs in, gets assigned a JWT, and
     * view the Order with the specific orderId.
     *
     * @throws Exception if the perform request or expect actions fail.
     */
    @Test
    public void testGetOrderByIdWithAuthentication() throws Exception {
        String jwt = authenticateAndGetJwt();
        mockMvc.perform(get("/api/orders/{id}", order.getOrderId())
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()))
                .andExpect(jsonPath("$.orderStatus").value("DELIVERED"));
    }
}
