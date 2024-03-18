package wms.rest.wms.model;

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
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "status", nullable = false)
    private String status; //packed, in transit, deliverd (?)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<CustomerOrderQuantities> quantities = new LinkedHashSet<>();

    /**
     * Constructor for order.
     *
     * @param date the time of the order was placed.
     * @param status the delivery status of the order.
     * @param user the user of which the order was placed by.
     */
    public Order(Date date, String status, User user){
        this.date = date;
        this.status = status;
        this.user = user;
    }

    /**
     * Empty constructor.
     */
    public Order(){
    }
}
