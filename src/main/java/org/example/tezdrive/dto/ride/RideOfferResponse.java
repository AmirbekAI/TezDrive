package org.example.tezdrive.dto.ride;

import lombok.Builder;
import lombok.Data;
import org.example.tezdrive.dto.car.CarResponse;
import org.example.tezdrive.entity.RideStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RideOfferResponse {

    private Long id;
    private Long driverId;
    private String driverName;
    private double driverRating;
    private int driverRatingCount;
    private String fromCity;
    private String toCity;
    private LocalDateTime departureTime;
    private int availableSeats;
    private BigDecimal pricePerSeat;
    private String notes;
    private RideStatus status;
    private CarResponse car;
}
