package wms.rest.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "dimension", nullable = false)
    private double dimensionCm3;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Measurement(double weight, double dimensionCm3){
        this.weight = weight;
        this.dimensionCm3 = dimensionCm3;
    }

    public Measurement(){
    }
}