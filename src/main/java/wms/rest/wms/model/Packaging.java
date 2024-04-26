package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class represents the packaging details for each product. This class defines the attributes and characteristics
 * of product packaging, including the type of packaging (D_PAK or F_PAK), quantity per package,
 * weight and dimensions.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "packaging")
public class Packaging {

    /**
     * Nested enum representing different types of packaging for products.
     * Mainly for easier readability.
     */
    public enum PackageType{
        D_PAK,
        F_PAK
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packaging_id", nullable = false)
    private int packagingId;

    @NotBlank(message = "Package type is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name = "package_type")
    private PackageType packageType;

    @Min(1)
    @Column(name = "quantity_pr_package")
    private int quantityPrPackage;

    @Min(1)
    @Column(name = "weight_in_gram", nullable = false)
    private double weightInGrams;

    @Min(1)
    @Column(name = "dimension_cm_3", nullable = false)
    private double dimensionInCm3;

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
}