package org.example.tezdrive.dto.ride;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RideSearchRequest {

    @NotBlank
    private String fromCity;

    @NotBlank
    private String toCity;

    @NotNull
    private LocalDate date;

    @Min(1)
    private int minSeats = 1;

    @DecimalMin("0.01")
    private BigDecimal maxPrice = new BigDecimal("999999");
}
