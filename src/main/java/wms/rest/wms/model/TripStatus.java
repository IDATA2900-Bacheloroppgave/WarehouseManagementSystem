package wms.rest.wms.model;

/**
 * Represents the various states a trip can undergo in the delivery process.
 * Once the OrderStatus as been set to PICKED, the TripStatus can change state
 * from NOT_STARTED to READY_FOR_DEPARTURE.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public enum TripStatus {
    /** Represents the first stage once a Trip is made */
    NOT_STARTED,

    /** Represents a stage where the trailer is ready for departure */
    READY_FOR_DEPARTURE,

    /** Represents a stage where the trailer is active and ready for loading */
    ACTIVE,

    /** Represents a stage where the goods are loaded on to the trailer */
    LOADING,

    /** Represents a stage where the trailer has departed */
    DEPARTED,

    /** Represents a stage where the trailer is in transit with all goods  */
    IN_TRANSIT,

    /** Represents the last stage of the Trip, and the Trip is finished */
    FINISHED
}
