package wms.rest.wms.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CustomerRepository.
 * Verifying that custom query methods work as expected.
 *
 * @author Mikkel Stavelie.
 */
@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    /** Autowired CustomerRepository for access to its methods in testing */
    @Autowired
    private CustomerRepository customerRepository;

    /** Autowired TestEntityManager for handling database operations directly in tests */
    @Autowired
    private TestEntityManager entityManager;

    /** Declare Customers at class level for easier accessibility */
    private Customer customer;
    private Customer customer2;

    /**
     * Prepare the test environment before each test method.
     * This method is run before each test method to ensure the testing environment is properly initialized.
     * Reduces boilerplate code and makes test more clear and concise.
     */
    @BeforeEach
    public void setup () {
        Store store = new Store();
        store.setName("Test Store");
        store.setAddress("123 Test St");
        store.setCountry("Testland");
        store.setCity("Testville");
        store.setPostalCode(12345);
        entityManager.persist(store);

        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPassword("secret");
        customer.setStore(store);
        entityManager.persist(customer);
        entityManager.flush();

        customer2 = new Customer();
        customer2.setEmail("test2@example.com");
        customer2.setFirstName("Anna");
        customer2.setLastName("Doe");
        customer2.setPassword("secret");
        customer2.setStore(store);
        entityManager.persist(customer2);
        entityManager.flush();
    }

    /**
     * Tests the functionality of finding a Customer by email.
     * Confirms the correct retrieval of Customer details and verifies that a non-existent email return no Customer.
     */
    @Test
    public void testFindCustomerByEmail() {
        // Test finding an existing Customer by email
        Optional<Customer> foundCustomer = customerRepository.findByEmail("test@example.com");
        assertAll("Should fetch customer with correct details",
                () -> assertTrue(foundCustomer.isPresent(), "Customer should be found"),
                () -> assertEquals("John", foundCustomer.get().getFirstName(), "First name should match"),
                () -> assertEquals("Doe", foundCustomer.get().getLastName(), "Last name should match"),
                () -> assertEquals("test@example.com", foundCustomer.get().getEmail(), "Email should match")
        );

        // Test finding a non-existing customer by email
        Optional<Customer> notFoundCustomer = customerRepository.findByEmail("nonexistent@example.com");
        assertFalse(notFoundCustomer.isPresent(), "Customer should not be found");
    }
}