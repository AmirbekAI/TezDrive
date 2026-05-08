package org.example.tezdrive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.ride.CreateRideRequest;
import org.example.tezdrive.dto.ride.RideOfferResponse;
import org.example.tezdrive.dto.ride.RideSearchRequest;
import org.example.tezdrive.entity.User;
import org.example.tezdrive.security.UserPrincipal;
import org.example.tezdrive.service.RideOfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideOfferController {

    private final RideOfferService rideOfferService;

    // ---- Driver endpoints ----

    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideOfferResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateRideRequest request
    ) {
        User driver = principal.getUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(rideOfferService.create(driver, request));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<RideOfferResponse>> myRides(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(rideOfferService.getMyRides(principal.getUser().getId()));
    }

    // ---- Passenger endpoints ----

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RideOfferResponse>> search(
            @Valid @ModelAttribute RideSearchRequest request
    ) {
        return ResponseEntity.ok(rideOfferService.search(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideOfferResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rideOfferService.getById(id));
    }
}
