package wms.rest.wms.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.OrderStatus;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for OrderRepository.
 * Verifying that custom query methods work as expected.
 * @author Mikkel Stavelie.
 */
@DataJpaTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    /** Autowired OrderRepository for access to its methods in testing */
    @Autowired
    private OrderRepository orderRepository;

    /** Autowired TestEntityManager for handling database operations directly in tests */
    @Autowired
    private TestEntityManager entityManager;

    /** Declare Customer at class level for easier accessibility */
    private Customer customer;

    /** Declare Order at class level for easier accessibility */
    private Order order;

    /**
     * Prepare the test environment before each test method.
     * This method is run before each test method to ensure the testing environment is properly initialized.
     * Reduces boilerplate code and makes test more clear and concise.
     */
    @BeforeEach
    public void setup() {
        Store store = new Store();
        store.setName("Test store");
        store.setCity("Test City");
        store.setAddress("Test address");
        store.setPostalCode(5004);
        store.setCountry("Test Country");
        entityManager.persist(store);
        entityManager.flush();

        customer = new Customer();
        customer.setEmail("staveliem@gmail.com");
        customer.setFirstName("Mikkel");
        customer.setLastName("Stavelie");
        customer.setPassword("Supersecretpassword11");
        customer.setStore(store);
        entityManager.persist(customer);
        entityManager.flush();

        order = new Order();
        order.setProgressInPercent(20);
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setShipment(null);
        order.setWishedDeliveryDate(LocalDate.now());
        order.setCustomer(customer);
        entityManager.persist(order);
        entityManager.flush();
    }

    /**
     * Tests the findByCustomer method in OrderRepository.
     */
    @Test
    public void testFindByCustomer() {
        // Customer and Order is created beforehand and saved to their associated repository
        List<Order> orders = this.orderRepository.findByCustomer(customer);
        assertEquals(1, orders.size(), "There should be exactly one order for the customer");
    }

    /**
     * Tests the findById method in OrderRepository.
     */
    @Test
    public void testFindById() {
        Optional<Order> orderOptional = this.orderRepository.findById(order.getOrderId());
        assertTrue(orderOptional.isPresent());
        orderOptional.ifPresent(p -> assertEquals(customer, p.getCustomer()));
    }
}
