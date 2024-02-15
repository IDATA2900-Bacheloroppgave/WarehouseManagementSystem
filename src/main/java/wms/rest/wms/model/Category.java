package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Class represents a Category in the warehouse management system.
 */
@Entity
@Setter
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "categoryName", nullable = false)
    private String categoryName;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "Product_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    /**
     * Constructor.
     *
     * @param categoryName the name of the category.
     */
    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    /**
     * Empty constructor.
     */
    public Category(){
    }
}
