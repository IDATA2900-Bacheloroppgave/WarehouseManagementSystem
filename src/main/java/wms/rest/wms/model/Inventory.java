package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Stock is mandatory")
    @Column(name = "total_stock")
    private int totalStock;

    @NotNull(message = "Reserved stock is mandatory")
    @Column(name = "reserved_stock")
    private int reservedStock;

    @NotNull(message = "Available stock is mandatory")
    @Column(name = "available_stock")
    private int availableStock;

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
}