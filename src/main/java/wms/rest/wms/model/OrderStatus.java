package wms.rest.wms.model;

/**
 * Represents the status of an order inside the warehouse management system.
 * The order goes through several processes from REGISTED to PICKING to
 * PICKED. Once the order is PICKED, means that the TripStatus can change from
 * NOT_STARTED to READY_FOR_DEPARTURE. Once the TripStatus has the status FINISHED,
 * the OrderStatus changes from PICKED to DELIVERED.
 */
public enum OrderStatus {
    REGISTERED,
    PICKING,
    PICKED,
    DELIVERED,
    CANCELLED
}
