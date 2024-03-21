package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Class represents the Product in a warehouse management system. The product has several attributes such as
 * name, description, product type, gtin number and batch number for identification. The product also has a
 * one to one relation with the inventory for performance reasons. Once the product_id is located, the inventory
 * holds the stock of the product.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Description is mandatory")
    @Column(name = "description", nullable = false)
    private String description;

    @NotBlank(message = "Supplier is mandatory")
    @Column(name = "supplier", nullable = false)
    private String supplier;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    @Column(name = "best_before_date", nullable = false)
    private Date bestBeforeDate;

    @NotNull(message = "Product type is mandatory")
    @Column(name = "item", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Min(1)
    @Column(name = "price", nullable = false)
    private Double price;

    @Min(1)
    @Column(name = "gtin", nullable = false)
    private int gtin;

    @Min(1)
    @Column(name = "batch", nullable = false)
    private int batch;

    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE, optional = false, orphanRemoval = true)
    private Inventory inventory;

    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE, optional = false, orphanRemoval = true)
    private Packaging packaging;
}
