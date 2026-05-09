package org.example.tezdrive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.car.CarRequest;
import org.example.tezdrive.dto.car.CarResponse;
import org.example.tezdrive.security.UserPrincipal;
import org.example.tezdrive.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    // ---- Driver: add a car ----
    @PostMapping
    public ResponseEntity<CarResponse> addCar(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CarRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(carService.addCar(principal.getUser(), request));
    }

    // ---- Driver: list my cars ----
    @GetMapping("/my")
    public ResponseEntity<List<CarResponse>> myCars(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(carService.getMyCars(principal.getUser()));
    }

    // ---- Driver: delete a car ----
    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteCar(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long carId
    ) {
        carService.deleteCar(principal.getUser(), carId);
        return ResponseEntity.noContent().build();
    }
}
