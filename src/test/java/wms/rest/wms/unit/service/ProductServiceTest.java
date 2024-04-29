package wms.rest.wms.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wms.rest.wms.model.Product;
import wms.rest.wms.model.ProductType;
import wms.rest.wms.repository.ProductRepository;
import wms.rest.wms.service.ProductService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 * Test class for ProductService using Mockito framework.
 *
 * @author Mikkel Stavelie.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    /** Mocked instance of ProductRepository for handling Product data access in tests */
    @Mock
    private ProductRepository productRepository;

    /** Mocked instance of ProductService with mocks for testing business logic */
    @InjectMocks
    private ProductService productService;

    /**
     * Tests the retrieval of all Products in ProductService.
     * Ensures the ProductRepository's findAll method is called and correctly retrieves a List of all Products.
     * Verifies the correct number of Products are returned.
     *
     * @throws ParseException if the beginning of the String can't be parsed.
     */
    @Test
    public void testGetProducts() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fixedDate = dateFormat.parse("2024-08-24");

        when(productRepository.findAll()).thenReturn(Arrays.asList(
                new Product(1, "Sample Product", "Sample Description", "Sample Supplier", fixedDate, ProductType.DRY_GOODS, 10.99, 54321, 78905, null, null),
                new Product(2, "Sample Product2", "Sample Description2", "Sample Supplier2", fixedDate, ProductType.REFRIGERATED_GOODS, 10.50, 54321, 52135, null, null)
        ));

        // Method to test
        List<Product> products = productService.getProducts();

        // Verify the interaction with the mock
        verify(productRepository).findAll();
        assertEquals("Products should contains two products", 2, products.size());
    }

    /**
     * Tests the retrieval of Products with the same search query.
     */
    @Test
    public void testFindByNameContaining() {
        // Arrange
        String searchTerm = "Test";
        List<Product> mockProducts = Arrays.asList(
                new Product(1, "Test Product", "Description", "Supplier", new Date(), ProductType.DRY_GOODS, 20.00, 1000, 2000, null, null),
                new Product(2, "Another Test Product", "Description2", "Supplier2", new Date(), ProductType.REFRIGERATED_GOODS, 25.00, 2001, 3000, null, null)
        );
        when(productRepository.findByNameContaining(searchTerm)).thenReturn(mockProducts);

        // Act
        List<Product> foundProducts = productService.findByNameContaining(searchTerm);

        // Assert
        verify(productRepository).findByNameContaining(searchTerm);
        assertEquals("Should return a list of products containing the term", mockProducts, foundProducts);
    }

    /**
     * Tests the creation of a new Product.
     */
    @Test
    public void testCreateProduct() {
        // Setup
        Product testProduct = new Product(1, "Test Product", "Description", "Supplier", new Date(), ProductType.DRY_GOODS, 20.00, 1000, 2000, null, null);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Execution
        Product returnedProduct = productService.createProduct(testProduct);

        // Verification
        verify(productRepository).save(testProduct);
        assertEquals("Returned product should be the same as the test product", testProduct, returnedProduct);
    }
}
