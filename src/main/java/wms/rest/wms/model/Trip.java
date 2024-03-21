package wms.rest.wms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class represents a Trip within the warehouse management system, encapsulating the details about each trip,
 * including status, start and end locations, dates, driver information and associated shipments. One trip
 * can contain many shipments to several locations.
 */
@NoArgsConstructor
@AllArgsConstructor
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

    @NotNull(message = "Trip status is mandatory")
    @Column(name = "trip_status")
    @Enumerated(EnumType.STRING)
    private TripStatus tripStatus;

    @NotBlank(message = "Trip start location is mandatory")
    @Column(name = "start_location", nullable = false)
    private String tripStartLocation;

    @NotBlank
    @Column(name = "end_location", nullable = false)
    private String tripEndLocation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    @Column(name = "start_date", nullable = false)
    private Date tripStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    @Column(name = "end_date", nullable = false)
    private Date tripEndDate;

    @NotBlank(message = "Trip driver is mandatory")
    @Column(name = "driver", nullable = false)
    private String tripDriver;

    @Min(1)
    @Column(name = "phone", nullable = false)
    private int tripDriverPhone;

    @JsonManagedReference
    @OneToMany(mappedBy = "trip")
    private Set<Shipment> shipments = new LinkedHashSet<>();
}