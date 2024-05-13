package wms.rest.wms.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Inventory;
import wms.rest.wms.model.Packaging;
import wms.rest.wms.model.Product;
import wms.rest.wms.repository.InventoryRepository;
import wms.rest.wms.repository.PackagingRepository;
import wms.rest.wms.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class for product api controller.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Service
@AllArgsConstructor
public class ProductService {

    /**
     * Repository for handling Product persistence operations
     */
    private ProductRepository productRepository;

    private InventoryRepository inventoryRepository;

    private PackagingRepository packagingRepository;

    /**
     * Creates a Product and saves it to the repository.
     *
     * @param product the Product to create and save to the repository.
     * @return saves the Product to the repository. If the Product does not exist in the repository,
     * save and persist the new Product. If the incoming JSON payload of the Product already exist
     * in the repository, merge the new Product with the old.
     */
    public Product createProduct(Product product) {
        return this.productRepository.save(product);
    }

    /**
     * Return a List of all Products.
     *
     * @return a List of all Products
     */
    public List<Product> getProducts() {
        return this.productRepository.findAll();
    }

    /**
     * Search the repository for a Product or Products containing the name search query.
     *
     * @param name the name of the Product or Products to get.
     * @return a List of Products that contains the name search query.
     */
    public List<Product> findByNameContaining(String name) {
        return this.productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Search the repository for a Product containing the GTIN search query.
     *
     * @param gtin the GTIN of the Product to get.
     * @return an Optional object of the Product, can be present or not.
     */
    public Optional<Product> findByGtin(int gtin) {
        return this.productRepository.findByGtin(gtin);
    }

    /**
     * Check if a Product exists by productId.
     *
     * @param productId the productId of the Product to check if exists.
     * @return true if the Product exists, false otherwise.
     */
    public boolean existsById(int productId) {
        return this.productRepository.existsById(productId);
    }

    /**
     * Delete a Product by productId.
     *
     * @param productId the productId of the Product to delete.
     */
    public void deleteById(int productId) {
        this.productRepository.deleteById(productId);
    }

    /**
     * Return an Optional of Product.
     *
     * @param productId the productId of the Product to return.
     * @return an Optional object of Product, can be present or not.
     */
    public Optional<Product> findById(int productId) {
        return this.productRepository.findById(productId);
    }

    /**
     * Updates a Product by specified properties.
     *
     * @param productId the productId of the Product to update.
     * @param updatedProduct the incoming Product with new properties.
     * @return saves the new Product to the repository.
     */
    @Transactional
    public Product updateProduct(int productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        if (updatedProduct.getName() != null) {
            existingProduct.setName(updatedProduct.getName());
        }
        if (updatedProduct.getDescription() != null) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getSupplier() != null) {
            existingProduct.setSupplier(updatedProduct.getSupplier());
        }
        if (updatedProduct.getBestBeforeDate() != null) {
            existingProduct.setBestBeforeDate(updatedProduct.getBestBeforeDate());
        }
        if (updatedProduct.getProductType() != null) {
            existingProduct.setProductType(updatedProduct.getProductType());
        }
        if (updatedProduct.getPrice() != null) {
            existingProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getGtin() != 0) {
            existingProduct.setGtin(updatedProduct.getGtin());
        }
        if (updatedProduct.getBatch() != 0) {
            existingProduct.setBatch(updatedProduct.getBatch());
        }

        if (updatedProduct.getInventory() != null) {
            Inventory updatedInventory = updatedProduct.getInventory();
            Inventory existingInventory = existingProduct.getInventory();
            if (updatedInventory.getTotalStock() != 0) {
                existingInventory.setTotalStock(updatedInventory.getTotalStock());
            }
            if (updatedInventory.getReservedStock() != 0) {
                existingInventory.setReservedStock(updatedInventory.getReservedStock());
            }
            if (updatedInventory.getAvailableStock() != 0) {
                existingInventory.setAvailableStock(updatedInventory.getAvailableStock());
            }
            inventoryRepository.save(existingInventory);
        }

        if (updatedProduct.getPackaging() != null) {
            Packaging updatedPackaging = updatedProduct.getPackaging();
            Packaging existingPackaging = existingProduct.getPackaging();
            if (updatedPackaging.getPackageType() != null) {
                existingPackaging.setPackageType(updatedPackaging.getPackageType());
            }
            if (updatedPackaging.getQuantityPrPackage() != 0) {
                existingPackaging.setQuantityPrPackage(updatedPackaging.getQuantityPrPackage());
            }
            if (updatedPackaging.getWeightInGrams() != 0) {
                existingPackaging.setWeightInGrams(updatedPackaging.getWeightInGrams());
            }
            if (updatedPackaging.getDimensionInCm3() != 0) {
                existingPackaging.setDimensionInCm3(updatedPackaging.getDimensionInCm3());
            }
            packagingRepository.save(existingPackaging);
        }

        return productRepository.save(existingProduct);
    }
}