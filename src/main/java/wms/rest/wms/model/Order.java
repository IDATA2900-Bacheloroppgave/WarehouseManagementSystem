package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents an order in the warehouse management system. This entity maps to the 'customerOrder' table
 * in the database and captures the essential details of an order, including the order date, status,
 * and associations to the user who placed the order and the address for the order. It also includes a set of
 * CustomerOrderQuantities to represent the many-to-many relationship between orders and products, capturing the
 * quantity of each product in the order.
 */
@Getter
@Setter
@Entity
@Table(name = "customerOrder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "date", nullable = false)
    private Date orderDate;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<OrderQuantities> quantities = new LinkedHashSet<>();


    /**
     * Constructor for order.
     *
     * @param orderDate the time of the order was placed.
     * @param customer the user of which the order was placed by.
     */
    public Order(Date orderDate,OrderStatus orderStatus,  Customer customer){
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.customer = customer;
    }

    /**
     * Empty constructor.
     */
    public Order(){
    }
}

