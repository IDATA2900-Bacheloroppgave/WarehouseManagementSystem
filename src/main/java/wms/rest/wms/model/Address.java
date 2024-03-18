package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class represents the address in the system. This class is a part of the model layer and maps to the
 * 'address' table in the database. It holds information about a user's address, including street addresses,
 * country, city and postal code. Each address is associated with a User entity in a many to one relationship,
 * indicating that multiple addresses can be linked to a single user.
 */
@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "country", nullable = false, length = 70)
    private String country;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "postal_code", nullable = false, length = 30)
    private int postalCode;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Constructor for Address.
     *
     * @param address the address of the user.
     * @param country the country where the user is located.
     * @param city the city which the user is located.
     * @param postalCode the postal code of the city where the user is located.
     */
    public Address(String address,
                   String country, String city,
                   int postalCode) {
        this.address = address;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
    }

    /**
     * Empty constructor.
     */
    public Address(){
    }
}