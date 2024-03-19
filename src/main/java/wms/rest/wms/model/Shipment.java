package wms.rest.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    //Dato?
    //Ønsket levering?

    @Column(name = "shipment_load")
    private String shipment_load;

    @Column(name = "shipment_unload")
    private String shipment_unload;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToMany
    private Set<Order> orders = new LinkedHashSet<>();

}