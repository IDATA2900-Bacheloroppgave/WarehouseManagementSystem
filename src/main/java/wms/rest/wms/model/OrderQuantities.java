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
    @Column(name = "customer_order_quantities_id", nullable = false)
    private int customerOrderQuantitiesId;

    @Column(name = "product_quantity")
    private int productQuantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Co
     *
     * @param product
     * @param order
     * @param productQuantity
     */
    public OrderQuantities(Product product, Order order, int productQuantity) {
        this.product = product;
        this.order = order;
        this.productQuantity = productQuantity;
    }

    public OrderQuantities(){
    }

}