package org.example.tezdrive.dto.ride;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateRideRequest {

    @NotBlank
    private String fromCity;

    @NotBlank
    private String toCity;

    @NotNull
    @Future
    private LocalDateTime departureTime;

    @Min(1)
    private int availableSeats;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal pricePerSeat;

    private String notes;
}
