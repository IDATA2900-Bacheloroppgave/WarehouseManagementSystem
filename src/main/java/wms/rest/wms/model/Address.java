package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class represents the address in the warehouse management system. This class is a part of the model layer and maps to the
 * 'address' table in the database. It holds information about a user's address, including street addresses,
 * country, city and postal code. Each address is associated with a User entity in a many to one relationship,
 * indicating that multiple addresses can be linked to a single user.
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private int addressId;

    @NotBlank(message = "Address is mandatory")
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank(message = "Country is mandatory")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "City is mandatory")
    @Column(name = "city", nullable = false)
    private String city;

    @Min(1)
    @Column(name = "postal_code", nullable = false)
    private int postalCode;

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;
}