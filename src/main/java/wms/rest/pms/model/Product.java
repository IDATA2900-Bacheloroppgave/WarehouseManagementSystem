package wms.rest.pms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "Item", nullable = false)
    private Item item;

    @Column(name = "Inventory", nullable = false)
    private int inventory;

    @Column(name = "gtin", nullable = false)
    private int gtin;

    @Column(name = "batch", nullable = false)
    private int batch;

    public Product(int id, String name, String description, Item item
            , int inventory, int gtin, int batch){
    }

    public Product(){

    }
}
