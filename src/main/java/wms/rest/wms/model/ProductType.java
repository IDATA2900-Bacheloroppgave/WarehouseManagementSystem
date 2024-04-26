package wms.rest.wms.model;

/**
 * Represents the type of product in the inventory.
 * This enum defines different categories of products such as dry goods,
 * refrigerated goods and frozen goods as they individually require
 * specific storage conditions.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public enum ProductType {

    /** Dry goods, temperature 10-20 degrees */
    DRY_GOODS,

    /** Refrigerated goods, temperature 0-4 degrees */
    REFRIGERATED_GOODS,

    /** Froze goods, temperature -18 degrees or lower */
    FROZEN_GOODS
}
