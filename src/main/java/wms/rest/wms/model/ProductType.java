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
    DRY_GOODS,
    REFRIGERATED_GOODS,
    FROZEN_GOODS
}
