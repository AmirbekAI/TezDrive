package org.example.tezdrive.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_photos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(nullable = false)
    private String url;
}
