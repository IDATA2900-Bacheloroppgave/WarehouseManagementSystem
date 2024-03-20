package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class Inventory {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id", nullable = false)
    private int id;

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(name = "stock")
    private int stock;

    /**
     * Constructor for Inventory.
     *
     * @param product the product of which the stock is associated with.
     * @param stock the current stock of the product.
     */
    public Inventory(Product product, int stock) {
        this.product = product;
        this.stock = stock;
    }

    /**
     * Empty constructor.
     */
    public Inventory(){
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}