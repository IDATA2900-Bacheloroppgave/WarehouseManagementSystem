package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class represents the Inventory of a product in the warehouse management system. The inventory
 * tracks the total stock, available stock and reserved stock.
 * availableStock = totalStock - reservedStock
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
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

    @Min(0)
    @Column(name = "total_stock")
    private int totalStock;

    @Min(0)
    @Column(name = "reserved_stock")
    private int reservedStock;

    @Min(0)
    @Column(name = "available_stock")
    private int availableStock;

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
}