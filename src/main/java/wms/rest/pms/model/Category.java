package wms.rest.pms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "categoryName", nullable = false)
    private String categoryName;

    @ManyToMany
    @JoinTable(
            name = "Product_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();


    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    public Category(){
    }



}
