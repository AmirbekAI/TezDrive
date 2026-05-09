package org.example.tezdrive.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Column(nullable = false)
    private String make;       // e.g. Toyota

    @Column(nullable = false)
    private String model;      // e.g. Camry

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false, unique = true)
    private String plateNumber;
}
