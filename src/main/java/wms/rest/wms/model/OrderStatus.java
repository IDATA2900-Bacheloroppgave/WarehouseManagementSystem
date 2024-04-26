package wms.rest.wms.model;

/**
 * Represents the status of an Order inside the Warehouse management system.
 * The Order goes through several processes from REGISTERED to PICKING to
 * PICKED. Once the order is PICKED, means that the TripStatus can change from
 * NOT_STARTED to READY_FOR_DEPARTURE. Once the TripStatus has the status FINISHED,
 * the OrderStatus changes from PICKED to DELIVERED.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public enum OrderStatus {

    /** Represents the first stage once a Order is made */
    REGISTERED,

    /** Represents a stage where the Order is being picked */
    PICKING,

    /** Represents a stage where the Order is fully picked */
    PICKED,

    /** Represents a stage where the Order is delivered to the Customer */
    DELIVERED,

    /** Represents a stage where the Order was cancelled by the Customer */
    CANCELLED
}
