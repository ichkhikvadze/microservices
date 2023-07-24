package com.example;

import com.example.enums.CarType;
import com.example.enums.DistanceUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(schema = "service_agent")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_id_gen")
    @SequenceGenerator(name = "car_id_gen", sequenceName = "car_id_seq", allocationSize = 1)
    private long id;
    @Column(name = "license_plate", unique = true, nullable = false)
    @Pattern(regexp = "[A-Z]{2}-\\d{3}-[A-Z]{2}")
    private String licensePlate;
    @Column(name = "vin_code", unique = true, nullable = false)
    @Pattern(regexp = "(?=.*\\d|=.*[A-Z])(?=.*[A-Z])[A-Z0-9]{17}")
    private String vinCode;
    @Column(name = "release_date", updatable = false, nullable = false)
    @Past
    @NotNull
    private LocalDate releaseDate;
    @Column(name = "car_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private CarType type;
    @Column(name = "odometer_value", nullable = false)
    @Min(0)
    private int odometerValue;
    @Column(name = "odometer_unit", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private DistanceUnit odometerUnit;
    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Owner owner;
}
