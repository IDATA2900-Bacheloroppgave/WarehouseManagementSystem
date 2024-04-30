package wms.rest.wms.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import wms.rest.wms.model.Product;
import wms.rest.wms.model.ProductType;
import wms.rest.wms.repository.ProductRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProductRepository.
 *
 * @author Mikkel Stavelie.
 */
@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    /** Autowired ProductRepository for access to its methods in testing */
    @Autowired
    private ProductRepository productRepository;

    /** Autowired TestEntityManager for handling database operations directly in tests */
    @Autowired
    private TestEntityManager entityManager;

    /** Declare Products at class level for easier accessibility */
    private Product product1;
    private Product product2;

    /** Declare String search query name at class level for easier accessibility */
    private String searchQueryName;

    /** Declare int gtin query at class level for easier accessibility */
    private int gtinQuery;

    /**
     * Prepare the test environment before each test method.
     * This method is run before each test method to ensure the testing environment is properly initialized.
     * Reduces boilerplate code and makes test more clear and concise.
     */
    @BeforeEach
    public void setup() {
        searchQueryName = "makrell";
        gtinQuery = 54321;

        product1 = new Product();
        product1.setName("Makrell i tomat");
        product1.setDescription("Sample Description");
        product1.setSupplier("Sample Supplier");
        product1.setBestBeforeDate(new Date());
        product1.setProductType(ProductType.DRY_GOODS);
        product1.setPrice(20.1);
        product1.setGtin(54321);
        product1.setBatch(12345);
        entityManager.persist(product1);
        entityManager.flush();

        product2 = new Product();
        product2.setName("tomatmakrell");
        product2.setDescription("Another Description");
        product2.setSupplier("Another Supplier");
        product2.setBestBeforeDate(new Date());
        product2.setProductType(ProductType.FROZEN_GOODS);
        product2.setPrice(15.99);
        product2.setGtin(12345);
        product2.setBatch(67890);
        entityManager.persist(product2);
        entityManager.flush();
    }

    /**
     * Tests the retrieval of all Products from the database.
     * Verifies that the correct number of Products is retrieved.
     */
    @Test
    public void testFindAll() {
        List<Product> products = this.productRepository.findAll();
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
        assertNotEquals(5, products.size());
    }

    /**
     * Tests retrieval of Products by name search query.
     * Verifies that the correct number of Products is retrieved by the search query, containing
     * both capital and lowercase letters, and name search query in composed words.
     */
    @Test
    public void testFindByNameContainingIgnoreCase() {
        List<Product> products = this.productRepository.findByNameContainingIgnoreCase(searchQueryName);
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
        assertNotEquals(5, products.size());
    }

    /**
     * Tests the retrieval of a Product by GTIN search query.
     * Verifies that the correct Product is retrieved when queried in the database.
     */
    @Test
    public void testFindByGtin() {
        Optional<Product> productOptional = this.productRepository.findByGtin(gtinQuery);
        assertTrue(productOptional.isPresent(), "Product should be found with GTIN 54321");
        productOptional.ifPresent(p -> {
            assertEquals(gtinQuery, p.getGtin(), "GTIN of the found product should match the queried GTIN");
            assertEquals("Makrell i tomat", p.getName(), "Product name should match");
            assertEquals(20.1, p.getPrice(), 0.01, "Product price should match");
        });

        assertEquals(product1, productOptional.orElse(null), "The found product should be the same as the 'product1' created and persisted");
    }
}