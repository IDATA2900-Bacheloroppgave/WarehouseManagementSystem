package wms.rest.wms.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wms.rest.wms.model.Order;

import java.util.HashSet;
import java.util.Set;

/**
 * Class represents a User.
 */
@Getter
@Setter
@Entity
public class User { //TODO

    @Id
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new HashSet<>();

    /**
     * Constructor for user.
     *
     * @param email
     * @param password
     */
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    /**
     * Empty constructor.
     */
    public User() {
    }
}
