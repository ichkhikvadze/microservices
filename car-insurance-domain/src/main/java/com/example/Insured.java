package com.example;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(schema = "car_insurance")
public class Insured {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insured_id_gen")
    @SequenceGenerator(name = "insured_id_gen", sequenceName = "insured_id_seq", allocationSize = 1)
    private long id;
    @Column(name = "first_name", nullable = false)
    @NotBlank
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @NotBlank
    private String lastName;
    @Column(name = "birth_date", updatable = false, nullable = false)
    @NotNull
    @Past
    private LocalDate birthDate;
    @EqualsAndHashCode.Include
    @Column(name = "personal_no", unique = true, nullable = false)
    @Pattern(regexp = "[0-9]{11}")
    private String personalNo;
}
