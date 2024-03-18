package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Class represents the Product in a warehouse management system. The product has several attributes such as
 * name, description, product type, gtin number and batch number for identification. The product also has a
 * one to one relation with the inventory for performance reasons. Once the product_id is located, the inventory
 * holds the stock of the product.
 */
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "Item", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "gtin", nullable = false)
    private int gtin;

    @Column(name = "batch", nullable = false)
    private int batch;

    @JsonIgnore
    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE, optional = false, orphanRemoval = true)
    private Inventory inventory;

    /**
     * Constructor for product.
     *
     * @param name the name of the product.
     * @param description the description of the product.
     * @param productType the type of item the product is.
     * @param gtin global trade item number.
     * @param batch batch number.
     */
    public Product(String name
                , String description
                , ProductType productType
                , int gtin
                , int batch){
        this.name = name;
        this.description = description;
        this.productType = productType;
        this.gtin = gtin;
        this.batch = batch;
    }

    /**
     * Empty constructor.
     */
    public Product(){
    }
}
