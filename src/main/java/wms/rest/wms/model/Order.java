package wms.rest.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wms.rest.wms.user.User;

import java.util.Date;

/**
 * Class represents the Order in a warehouse management system.
 */
@Getter
@Setter
@Entity
@Table(name = "'order'") //Order is reserved in MariaDB.
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date date;

    private String status; //packed, in transit, deliverd (?)

    @ManyToOne
    private User user;

    /**
     * Constructor for order.
     *
     * @param date the time of the order was placed.
     * @param status the delivery status of the order.
     * @param user the user of which the order was placed by.
     */
    public Order(Date date, String status,User user){
        this.date = date;
        this.status = status;
        this.user = user;
    }

    public Order(){
    }
}
