package wms.rest.wms.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wms.rest.wms.api.model.LoginBody;
import wms.rest.wms.api.model.RegistrationBody;
import wms.rest.wms.exception.CustomerAlreadyExistsException;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.CustomerRepository;
import wms.rest.wms.repository.StoreRepository;
import wms.rest.wms.service.CustomerService;
import wms.rest.wms.service.security.EncryptionService;
import wms.rest.wms.service.security.JwtService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for CustomerService using Mockito framework.
 *
 * @author Mikkel Stavelie.
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    /** Mocked instance of CustomerRepository for handling Customer data access in tests */
    @Mock
    private CustomerRepository customerRepository;

    /** Mocked instance of EncryptionService for simulating encryption operations in tests */
    @Mock
    private EncryptionService encryptionService;

    /** Mocked instance of StoreRepository for handling Store data access in tests */
    @Mock
    private StoreRepository storeRepository;

    /** Mocked instance of CustomerService with mocks for testing business logic  */
    @InjectMocks
    private CustomerService customerService;

    /** Mocked instance of JwtService for simulating JWT generation and verification in tests */
    @Mock
    private JwtService jwtService;

    /**
     * Tests the retrieval of all Customers in CustomerService.
     * Ensures the CustomerRepository's findAll method is called and correctly retrieves a List of all Customers.
     * Verifies the correct number of Customers are returned.
     */
    @Test
    public void testGetCustomers() {
        Store store = new Store();
        store.setName("Test Store");
        store.setAddress("123 Test St");
        store.setCountry("Testland");
        store.setCity("Testville");
        store.setPostalCode(12345);

        // Setup mock to return specific data
        when(customerRepository.findAll()).thenReturn(Arrays.asList(
                new Customer(1, "Johndoe@gmail.com", "John", "Doe", "secretpassword11", store),
                new Customer(2, "jahndoe@gmail.com", "Jahn", "Doe", "secretpassword11", store)
        ));

        // Method to test
        List<Customer> customers = customerService.getCustomers();

        verify(customerRepository).findAll();
        assertEquals(2, customers.size());
    }

    /**
     * Tests the retrieval of returning a Customer by customerId in CustomerService.
     * Ensures the CustomerRepository's findById method is called and correctly returns a Customer by the specified customerId.
     * Verifies that the correct Customer is returned with the specified customerId.
     */
    @Test
    public void testGetCustomerById() {
        int customerId = 1;
        Customer expectedCustomer = new Customer();
        expectedCustomer.setCustomerId(customerId);
        expectedCustomer.setEmail("example@example.com");
        expectedCustomer.setFirstName("John");
        expectedCustomer.setLastName("Doe");
        expectedCustomer.setPassword("secretpassword");
        expectedCustomer.setStore(new Store());

        // Setup mock response
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        // Act
        Optional<Customer> actualCustomer = customerService.getCustomerById(customerId);

        // Assert
        assertTrue(actualCustomer.isPresent(), "Customer should be present");
        assertEquals(expectedCustomer, actualCustomer.get(), "Expected and actual customer should match");
        verify(customerRepository).findById(customerId);
    }

    /**
     * Tests the registration of a new Customer by using a RegistrationBody.
     * Verifies that no exceptions are thrown when a non-existent email is used and a valid storeId is used.
     * Confirms that the CustomerRepository checks for the email, retrieves the store, and saves the new Customer.
     */
    @Test
    public void testRegisterCustomer_NewCustomer() {
        RegistrationBody registrationBody = new RegistrationBody("test@example.com", "Test", "User", "password123", 1);
        when(customerRepository.findByEmailIgnoreCase(registrationBody.getEmail())).thenReturn(Optional.empty());
        Store store = new Store();
        store.setStoreId(1);
        when(storeRepository.findById(registrationBody.getStoreId())).thenReturn(Optional.of(store));
        when(encryptionService.encryptPassword("password123")).thenReturn("encryptedPassword");

        // Act
        assertDoesNotThrow(() -> customerService.registerCustomer(registrationBody));

        // Assert
        verify(customerRepository).findByEmailIgnoreCase("test@example.com");
        verify(storeRepository).findById(1);
        verify(customerRepository).save(any(Customer.class));
    }

    /**
     * Verifies that attempting to register an already existing Customer throws CustomerExistsException.
     */
    @Test
    public void testRegisterCustomer_ExistingCustomer() {
        // Given
        RegistrationBody registrationBody = new RegistrationBody("test@example.com", "Test", "User", "password123", 1);
        when(customerRepository.findByEmailIgnoreCase(registrationBody.getEmail())).thenReturn(Optional.of(new Customer()));

        // Act & Assert
        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.registerCustomer(registrationBody));

        // Verify
        verify(customerRepository).findByEmailIgnoreCase("test@example.com");
        verify(storeRepository, never()).findById(anyInt());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    /**
     * Ensures registration fails and throws RunTimeException when the specified Store is not found.
     */
    @Test
    public void testRegisterCustomer_StoreNotFound() {
        // Given
        RegistrationBody registrationBody = new RegistrationBody("test@example.com", "Test", "User", "password123", 1);
        when(customerRepository.findByEmailIgnoreCase(registrationBody.getEmail())).thenReturn(Optional.empty());
        when(storeRepository.findById(registrationBody.getStoreId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> customerService.registerCustomer(registrationBody));
        assertEquals("Store with ID: 1 was not found", exception.getMessage());

        // Verify
        verify(customerRepository).findByEmailIgnoreCase("test@example.com");
        verify(storeRepository).findById(1);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    /**
     * Tests successful Customer login, verifying correct JWT generation when credentials are valid.
     */
    @Test
    public void testLoginCustomer_Successful() {
        // Given
        LoginBody loginBody = new LoginBody("test@example.com", "password123");
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("encryptedPassword");
        when(customerRepository.findByEmail(loginBody.getEmail())).thenReturn(Optional.of(customer));
        when(encryptionService.verifyPassword(loginBody.getPassword(), customer.getPassword())).thenReturn(true);
        when(jwtService.generateJWT(customer)).thenReturn("valid.jwt.token");

        // Act
        String jwt = customerService.loginCustomer(loginBody);

        // Assert
        assertNotNull(jwt, "JWT should not be null on successful login");
        assertEquals("valid.jwt.token", jwt, "JWT token should match the expected value");
        verify(encryptionService).verifyPassword("password123", "encryptedPassword");
    }

    /**
     * Tests the login process with an incorrect password, expecting no JWT to be generated.
     */
    @Test
    public void testLoginCustomer_UnsuccessfulPassword() {
        // Given
        LoginBody loginBody = new LoginBody("test@example.com", "password123");
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("encryptedPassword");
        when(customerRepository.findByEmail(loginBody.getEmail())).thenReturn(Optional.of(customer));
        when(encryptionService.verifyPassword(loginBody.getPassword(), customer.getPassword())).thenReturn(false);

        // Act
        String jwt = customerService.loginCustomer(loginBody);

        // Assert
        assertNull(jwt, "JWT should be null if the password is incorrect");
    }

    /**
     * Tests login attempt for non-existent Customer, expecting no JWT to be generated.
     */
    @Test
    public void testLoginCustomer_UnsuccessfulNoUser() {
        // Given
        LoginBody loginBody = new LoginBody("nonexistent@example.com", "password123");
        when(customerRepository.findByEmail(loginBody.getEmail())).thenReturn(Optional.empty());

        // Act
        String jwt = customerService.loginCustomer(loginBody);

        // Assert
        assertNull(jwt, "JWT should be null if the user does not exist");
    }
}
