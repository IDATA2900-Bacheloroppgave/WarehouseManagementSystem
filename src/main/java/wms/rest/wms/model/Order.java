package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customerOrder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private int orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    @Column(name = "date")
    private Date orderDate;

    @NotNull(message = "Order status is mandatory")
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "wished_delivery_date")
    private Date wishedDeliveryDate;

    @Column(name = "progress_in_percent")
    private int progressInPercent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;

    @JsonIgnore
    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderQuantities> quantities = new LinkedHashSet<>();
}

