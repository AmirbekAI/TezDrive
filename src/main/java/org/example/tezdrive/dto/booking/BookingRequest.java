package org.example.tezdrive.dto.booking;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BookingRequest {

    @Min(1)
    private int seats;
}
