package org.example.tezdrive.dto.car;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CarResponse {
    private Long id;
    private Long driverId;
    private String make;
    private String model;
    private int year;
    private String color;
    private String plateNumber;
    private List<String> photos;
}
