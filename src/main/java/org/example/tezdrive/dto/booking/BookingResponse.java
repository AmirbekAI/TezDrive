package org.example.tezdrive.dto.booking;

import lombok.Builder;
import lombok.Data;
import org.example.tezdrive.entity.BookingStatus;

@Data
@Builder
public class BookingResponse {

    private Long id;
    private Long rideId;
    private String fromCity;
    private String toCity;
    private Long passengerId;
    private String passengerName;
    // visible only when booking is ACCEPTED
    private String passengerPhone;
    private Long driverId;
    private String driverName;
    // visible only when booking is ACCEPTED
    private String driverPhone;
    private int seats;
    private BookingStatus status;
    private Integer rating;
}
