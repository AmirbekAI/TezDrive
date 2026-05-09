package org.example.tezdrive.dto.car;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarRequest {

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1900, message = "Year must be valid")
    @Max(value = 2100, message = "Year must be valid")
    private int year;

    @NotBlank(message = "Color is required")
    private String color;

    @NotBlank(message = "Plate number is required")
    private String plateNumber;
}
