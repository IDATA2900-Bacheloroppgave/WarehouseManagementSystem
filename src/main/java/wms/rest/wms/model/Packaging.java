package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "packaging")
public class Packaging {

    public enum PackageType{
        D_PAK,
        F_PAK
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packaging_id", nullable = false)
    private int packagingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_type")
    private PackageType packageType;

    @Column(name = "quantity_pr_package")
    private int quantityPrPackage;

    @Column(name = "weight_in_gram", nullable = false)
    private double weightInGrams;

    @Column(name = "dimension_cm_3", nullable = false)
    private double dimensionInCm3;

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    public Packaging(PackageType packageType, int quantityPrPackage, double weightInGrams, double dimensionInCm3, Product product) {
        this.packageType = packageType;
        this.quantityPrPackage = quantityPrPackage;
        this.weightInGrams = weightInGrams;
        this.dimensionInCm3 = dimensionInCm3;
        this.product = product;
    }

    public Packaging() {
    }
}