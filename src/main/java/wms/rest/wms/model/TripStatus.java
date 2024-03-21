package wms.rest.wms.model;

/**
 * Represents the various states a trip can undergo in the delivery process.
 * Once the OrderStatus as been set to PICKED, the TripStatus can change state
 * from NOT_STARTED to READY_FOR_DEPARTURE.
 */
public enum TripStatus {
    NOT_STARTED,
    READY_FOR_DEPARTURE,
    ACTIVE,
    LOADING,
    DEPARTED,
    IN_TRANSIT,
    FINISHED
}
