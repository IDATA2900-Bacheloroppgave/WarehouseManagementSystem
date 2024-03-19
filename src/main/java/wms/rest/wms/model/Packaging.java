package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "packaging")
public class Packaging {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "package_type")
    private String packageType; //D-PAK, F-PAK

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "weightInG", nullable = false)
    private double weight;

    @Column(name = "dimension_cm_3", nullable = false)
    private double dimensionCm3;

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    public Packaging(String packageType, int quantity, double weight, double dimensionCm3, Product product) {
        this.packageType = packageType;
        this.quantity = quantity;
        this.weight = weight;
        this.dimensionCm3 = dimensionCm3;
        this.product = product;
    }

    public Packaging() {
    }
}