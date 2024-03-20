package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tripId")
@Getter
@Setter
@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id", nullable = false)
    private int tripId;

    @Column(name = "trip_status")
    @Enumerated(EnumType.STRING)
    private TripStatus tripStatus;

    @Column(name = "start_location", nullable = false)
    private String tripStartLocation;

    @Column(name = "end_location", nullable = false)
    private String tripEndLocation;

    @Column(name = "start_date", nullable = false)
    private Date tripStartDate;

    @Column(name = "end_date", nullable = false)
    private Date tripEndDate;

    @Column(name = "driver", nullable = false)
    private String tripDriver;

    @Column(name = "phone", nullable = false)
    private int tripDriverPhone;

    @JsonManagedReference
    @OneToMany(mappedBy = "trip")
    private Set<Shipment> shipments = new LinkedHashSet<>();

    public Trip(TripStatus tripStatus, String tripStartLocation, String tripEndLocation, Date tripStartDate,
                Date tripEndDate, String tripDriver, int tripDriverPhone) {
        this.tripStatus = tripStatus;
        this.tripStartLocation = tripStartLocation;
        this.tripEndLocation = tripEndLocation;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.tripDriver = tripDriver;
        this.tripDriverPhone = tripDriverPhone;
    }

    public Trip(){
    }
}