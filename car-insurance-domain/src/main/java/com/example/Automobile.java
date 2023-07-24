package com.example;

import com.example.enums.AutomobileType;
import com.example.enums.InsuranceType;
import com.example.enums.OdometerUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(schema = "car_insurance")
public class Automobile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "automobile_id_gen")
    @SequenceGenerator(name = "automobile_id_gen", sequenceName = "automobile_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Include
    private long id;
    @Column(name = "release_date", updatable = false, nullable = false)
    @Past
    @NotNull
    private LocalDate releaseDate;
    @Column(name = "vin_code", nullable = false)
    @Pattern(regexp = "(?=.*\\d|=.*[A-Z])(?=.*[A-Z])[A-Z0-9]{17}")
    private String vinCode;
    @Column(name = "automobile_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private AutomobileType type;
    @Column(name = "odometer_value", nullable = false)
    @Min(0)
    private int odometerValue;
    @Column(name = "odometer_unit", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private OdometerUnit odometerUnit;
    @Column(name = "license_plate", unique = true, nullable = false)
    @Pattern(regexp = "[A-Z]{2}-\\d{3}-[A-Z]{2}")
    private String licensePlate;
    @Column(name = "full_insurance_price", nullable = false)
    private BigDecimal fullInsurancePrice;
    @Column(name = "insurance_pension", nullable = false)
    private BigDecimal insurancePension;
    @Column(name = "insurance_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private InsuranceType insuranceType;
    @Column(name = "franchise_price")
    private BigDecimal franchisePrice;
    @ManyToOne
    @JoinColumn(name = "insured_id", nullable = false)
    private Insured insured;
}
