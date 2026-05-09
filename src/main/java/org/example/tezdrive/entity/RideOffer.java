package org.example.tezdrive.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ride_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Column(nullable = false)
    private String fromCity;

    @Column(nullable = false)
    private String toCity;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private int availableSeats;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerSeat;

    @Column
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status;
}
