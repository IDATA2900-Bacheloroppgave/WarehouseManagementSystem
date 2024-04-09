package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class represents the Inventory of a product in the warehouse management system. The inventory
 * tracks the total stock, available stock and reserved stock.
 *
 * availableStock = totalStock - reservedStock
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "inventory")
public class Inventory {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id", nullable = false)
    private int inventoryId;

    @Positive
    @Column(name = "total_stock")
    private int totalStock;

    @Min(1)
    @Column(name = "reserved_stock")
    private int reservedStock;

    @Min(1)
    @Column(name = "available_stock")
    private int availableStock;

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
}