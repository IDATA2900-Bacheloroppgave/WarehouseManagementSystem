package wms.rest.pms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Class represents the Product in a warehouse management system.
 */
@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "Item", nullable = false)
    @Enumerated(EnumType.STRING)
    private Item item;

    @Column(name = "Inventory", nullable = false)
    private int inventory;

    @Column(name = "gtin", nullable = false)
    private int gtin;

    @Column(name = "batch", nullable = false)
    private int batch;

    @ManyToMany(mappedBy = "products")
    private Set<Category> categories = new HashSet<>();

    /**
     * Constructor for product.
     *
     * @param id the id of the product.
     * @param name the name of the product.
     * @param description the description of the product.
     * @param item the type of item the product is.
     * @param inventory how many of the product in inventory.
     * @param gtin global trade item number.
     * @param batch batch number.
     */
    public Product(int id
                , String name
                , String description
                , Item item
                , int inventory
                , int gtin
                , int batch){
        this.name = validateString(name, "name");
        this.description = validateString(description, "description");
        this.inventory = validateInteger(inventory, "inventory");
        this.gtin = validateInteger(gtin, "global trade item number");
        this.batch = validateInteger(batch, "batch");
    }

    /**
     * Empty constructor.
     */
    public Product(){
    }

    /**
     * Checks if a string is valid or not.
     * Return the string if valid, otherwise throw illegalargumentexception.
     *
     * @param value the string you want to validate.
     * @param parameterName the name used to identify the string in error messages.
     * @return the original string if it is valid.
     */
    public String validateString(String value, String parameterName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("The parameter '" + parameterName + "' cannot be null or blank.");
        }
        return value;
    }

    /**
     * Check if a int is valid or not.
     * Return the int if valid, otherwise throw illegalargumentexception.
     *
     * @param value the int you want to validate.
     * @param parameterName the name used to identify the int in error messages.
     * @return the original value if it is valid.
     */
    public int validateInteger(int value, String parameterName) {
        if(value < 0){
            throw new IllegalArgumentException("The parameter '" + parameterName + "' cannot be less than zero");
        }
        return value;
    }

    /**
     * Validate if product is valid or not.
     *
     * @param product the product to check if is valid.
     * @return true if valid, otherwise false.
     */
    public boolean isValid(Product product){
        return !" ".equals(name) && ! " ".equals(description);
    }
}
