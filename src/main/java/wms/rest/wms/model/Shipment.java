package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class represents a Shipping entity within the warehouse management system, detailing the specific attributes of
 * each shipment, including its load and unload points, associated trip and the orders associated
 * with the shipment. One shipment can consist of several orders.
 */
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "shipmentId")
@Getter
@Setter
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id", nullable = false)
    private int shipmentId;

    @Column(name = "shipment_start_location")
    private String shipmentStartLocation;

    @Column(name = "shipment_end_location")
    private String shipmentEndLocation;

    @NotBlank(message = "Shipment load is mandatory")
    @Column(name = "shipment_load")
    private String shipmentLoadLocation;

    @NotBlank(message = "Shipment unload is mandatory")
    @Column(name = "shipment_unload")
    private String shipmentUnloadLocation;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders = new LinkedHashSet<>();

    //Dato?
    //Ønsket levering?
}