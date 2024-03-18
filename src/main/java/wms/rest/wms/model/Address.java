package wms.rest.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Address(String address,
                   String country, String city,
                   int postalCode) {

        this.address = address;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
    }

    public Address(){
    }
}