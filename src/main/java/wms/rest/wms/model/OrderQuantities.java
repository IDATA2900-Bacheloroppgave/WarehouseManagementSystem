package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Class describes the relationship between customer orders and the products they contain.
 *
 */
@Getter
@Setter
@Entity
@Table(name = "customer_order_quantities")
public class OrderQuantities {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_quantities_id", nullable = false)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "quantity")
    private int quantity;

    /**
     * Co
     *
     * @param product
     * @param order
     * @param quantity
     */
    public OrderQuantities(Product product, Order order, int quantity) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
    }

    public OrderQuantities(){
    }

}