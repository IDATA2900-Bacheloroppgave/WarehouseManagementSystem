package wms.rest.wms.unit.repository;

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

    /**
     * Tests the retrieval of all Customers from the database.
     * Verifies that the correct number of Customers is retrieved.
     */
    @Test
    public void testFindAllCustomers() {
        Store store = new Store();
        store.setName("Test Store");
        store.setAddress("123 Test St");
        store.setCountry("Testland");
        store.setCity("Testville");
        store.setPostalCode(12345);
        store = entityManager.persist(store); // Store directly into database using TestEntityManager,

        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPassword("secret");
        customer.setStore(store);
        entityManager.persist(customer);
        entityManager.flush();

        Customer customer2 = new Customer();
        customer2.setEmail("test2@example.com");
        customer2.setFirstName("Anna");
        customer2.setLastName("Doe");
        customer2.setPassword("secret");
        customer2.setStore(store);
        entityManager.persist(customer2);
        entityManager.flush();

        List<Customer> customers = this.customerRepository.findAll();

        // Test finding all Customers from repository
        assertEquals(2, customers.size());
        assertTrue(customers.contains(customer));
        assertTrue(customers.contains(customer2));

        // Test finding wrong Customer amount from repository
        assertNotEquals(5, customers.size());
    }

    /**
     * Tests the functionality of finding a Customer by email.
     * Confirms the correct retrieval of Customer details and verifies that a non-existent email return no Customer.
     */
    @Test
    public void testFindCustomerByEmail() {
        // Create Store
        Store store = new Store();
        store.setName("Test Store");
        store.setAddress("123 Test St");
        store.setCountry("Testland");
        store.setCity("Testville");
        store.setPostalCode(12345);
        store = entityManager.persist(store);

        // Create Customer
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPassword("secret");
        customer.setStore(store);
        entityManager.persist(customer);
        entityManager.flush();

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