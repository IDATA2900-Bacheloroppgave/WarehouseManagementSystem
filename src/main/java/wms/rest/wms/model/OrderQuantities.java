package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class describes the relationship between customer orders and the products they contain.
 * This class defines the relationship between customer orders and the specific products
 * included within those orders.
 *
 * @author Mikkel Stavelie.,
 * @version 1.0.
 */
@NoArgsConstructor
@AllArgsConstructor
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

    @Min(1)
    @Column(name = "product_quantity")
    private int productQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;
}