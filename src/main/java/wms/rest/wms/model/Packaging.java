package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class represents the packaging details for each product. This class defines the attributes and characteristics
 * of product packaging, including the type of packaging (D_PAK or F_PAK), quantity per package,
 * weight and dimensions.
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

    @NotNull(message = "Quantity per package is mandatory")
    @Column(name = "quantity_pr_package")
    private int quantityPrPackage;

    @NotNull(message = "Weight is mandatory")
    @Column(name = "weight_in_gram", nullable = false)
    private double weightInGrams;

    @NotNull(message = "Dimensions is mandatory")
    @Column(name = "dimension_cm_3", nullable = false)
    private double dimensionInCm3;

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
}