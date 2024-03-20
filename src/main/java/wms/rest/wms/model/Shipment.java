package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedHashSet;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id", nullable = false)
    private int id;

    //Dato?
    //Ønsket levering?

    @Column(name = "shipment_load")
    private String shipment_load;

    @Column(name = "shipment_unload")
    private String shipment_unload;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders = new LinkedHashSet<>();

    public Shipment(String shipment_load, String shipment_unload, Trip trip) {
        this.shipment_load = shipment_load;
        this.shipment_unload = shipment_unload;
        this.trip = trip;
    }

     public Shipment() {
     }
}