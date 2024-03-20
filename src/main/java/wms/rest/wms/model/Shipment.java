package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedHashSet;
import java.util.Set;

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

    //Dato?
    //Ønsket levering?

    @Column(name = "shipment_load")
    private String shipmentLoad;

    @Column(name = "shipment_unload")
    private String shipmentUnload;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders = new LinkedHashSet<>();

    public Shipment(String shipmentLoad, String shipmentUnload, Trip trip) {
        this.shipmentLoad = shipmentLoad;
        this.shipmentUnload = shipmentUnload;
        this.trip = trip;
    }

     public Shipment() {
     }
}