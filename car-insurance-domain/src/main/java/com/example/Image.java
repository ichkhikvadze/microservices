package com.example;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_gen")
    @SequenceGenerator(name = "image_id_gen", sequenceName = "image_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Include
    private long id;
    @Column(name = "base_64_file")
    private String base64File;
    @ManyToOne
    @JoinColumn(name = "automobile_id", nullable = false)
    private Automobile automobile;
}
