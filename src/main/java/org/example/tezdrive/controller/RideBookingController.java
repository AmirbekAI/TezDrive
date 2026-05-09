package org.example.tezdrive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.booking.BookingRequest;
import org.example.tezdrive.dto.booking.BookingResponse;
import org.example.tezdrive.dto.booking.RateRequest;
import org.example.tezdrive.dto.comment.CommentRequest;
import org.example.tezdrive.dto.comment.CommentResponse;
import org.example.tezdrive.entity.User;
import org.example.tezdrive.security.UserPrincipal;
import org.example.tezdrive.service.CommentService;
import org.example.tezdrive.service.RideBookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RideBookingController {

    private final RideBookingService bookingService;
    private final CommentService commentService;

    // ---- Passenger: request a booking ----
    @PostMapping("/rides/{rideId}/bookings")
    public ResponseEntity<BookingResponse> requestBooking(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long rideId,
            @Valid @RequestBody BookingRequest request
    ) {
        User passenger = principal.getUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.requestBooking(passenger, rideId, request));
    }

    // ---- Passenger: view my bookings ----
    @GetMapping("/bookings/my")
    public ResponseEntity<List<BookingResponse>> myBookings(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(bookingService.getMyBookings(principal.getUser()));
    }

    // ---- Passenger: cancel a booking ----
    @PatchMapping("/bookings/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancel(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long bookingId
    ) {
        return ResponseEntity.ok(bookingService.cancelBooking(principal.getUser(), bookingId));
    }

    // ---- Driver: view bookings for a ride ----
    @GetMapping("/rides/{rideId}/bookings")
    public ResponseEntity<List<BookingResponse>> bookingsForRide(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long rideId
    ) {
        return ResponseEntity.ok(bookingService.getBookingsForRide(principal.getUser(), rideId));
    }

    // ---- Driver: accept a booking ----
    @PatchMapping("/bookings/{bookingId}/accept")
    public ResponseEntity<BookingResponse> accept(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long bookingId
    ) {
        return ResponseEntity.ok(bookingService.acceptBooking(principal.getUser(), bookingId));
    }

    // ---- Driver: reject a booking ----
    @PatchMapping("/bookings/{bookingId}/reject")
    public ResponseEntity<BookingResponse> reject(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long bookingId
    ) {
        return ResponseEntity.ok(bookingService.rejectBooking(principal.getUser(), bookingId));
    }

    // ---- Passenger: rate a finished ride ----
    @PatchMapping("/bookings/{bookingId}/rate")
    public ResponseEntity<BookingResponse> rate(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long bookingId,
            @Valid @RequestBody RateRequest request
    ) {
        return ResponseEntity.ok(bookingService.rateBooking(principal.getUser(), bookingId, request));
    }

    // ---- Passenger: leave a comment for the driver ----
    @PostMapping("/bookings/{bookingId}/comment")
    public ResponseEntity<CommentResponse> comment(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long bookingId,
            @Valid @RequestBody CommentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(principal.getUser(), bookingId, request));
    }
}
