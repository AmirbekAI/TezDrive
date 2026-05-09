package org.example.tezdrive.service;

import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.booking.BookingRequest;
import org.example.tezdrive.dto.booking.BookingResponse;
import org.example.tezdrive.dto.booking.RateRequest;
import org.example.tezdrive.entity.*;
import org.example.tezdrive.exception.AccessDeniedException;
import org.example.tezdrive.repository.RideBookingRepository;
import org.example.tezdrive.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RideBookingService {

    private final RideBookingRepository bookingRepository;
    private final RideOfferService rideOfferService;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponse requestBooking(User passenger, Long rideId, BookingRequest request) {
        if (passenger.getRole() != Role.USER) {
            throw new AccessDeniedException("Only passengers can request bookings");
        }
        RideOffer ride = rideOfferService.findById(rideId);

        if (ride.getStatus() != RideStatus.ACTIVE) {
            throw new IllegalStateException("Ride is not available for booking");
        }
        if (ride.getDriver().getId().equals(passenger.getId())) {
            throw new IllegalStateException("Driver cannot book their own ride");
        }
        if (request.getSeats() > ride.getAvailableSeats()) {
            throw new IllegalStateException("Not enough available seats");
        }
        if (bookingRepository.existsByRideIdAndPassengerIdAndStatusIn(
                rideId, passenger.getId(),
                List.of(BookingStatus.PENDING, BookingStatus.ACCEPTED, BookingStatus.IN_RIDE))) {
            throw new IllegalStateException("You already have an active booking for this ride");
        }

        RideBooking booking = RideBooking.builder()
                .ride(ride)
                .passenger(passenger)
                .seats(request.getSeats())
                .status(BookingStatus.PENDING)
                .build();

        return toResponse(bookingRepository.save(booking), passenger.getId());
    }

    @Transactional
    public BookingResponse acceptBooking(User driver, Long bookingId) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can accept bookings");
        }
        RideBooking booking = findBookingForDriver(driver.getId(), bookingId);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be accepted");
        }

        RideOffer ride = booking.getRide();
        if (ride.getAvailableSeats() < booking.getSeats()) {
            throw new IllegalStateException("Not enough seats left on this ride");
        }

        ride.setAvailableSeats(ride.getAvailableSeats() - booking.getSeats());
        booking.setStatus(BookingStatus.ACCEPTED);

        return toResponse(bookingRepository.save(booking), driver.getId());
    }

    @Transactional
    public BookingResponse rejectBooking(User driver, Long bookingId) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can reject bookings");
        }
        RideBooking booking = findBookingForDriver(driver.getId(), bookingId);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be rejected");
        }

        booking.setStatus(BookingStatus.REJECTED);
        return toResponse(bookingRepository.save(booking), driver.getId());
    }

    @Transactional
    public BookingResponse cancelBooking(User passenger, Long bookingId) {
        if (passenger.getRole() != Role.USER) {
            throw new AccessDeniedException("Only passengers can cancel bookings");
        }
        RideBooking booking = bookingRepository.findByIdAndPassengerId(bookingId, passenger.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getRide().getStatus() != RideStatus.ACTIVE) {
            throw new IllegalStateException("Cannot cancel after the ride has started");
        }
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new IllegalStateException("This booking cannot be cancelled");
        }

        if (booking.getStatus() == BookingStatus.ACCEPTED) {
            booking.getRide().setAvailableSeats(booking.getRide().getAvailableSeats() + booking.getSeats());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return toResponse(bookingRepository.save(booking), passenger.getId());
    }

    public List<BookingResponse> getBookingsForRide(User driver, Long rideId) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can view ride bookings");
        }
        RideOffer ride = rideOfferService.findById(rideId);
        if (!ride.getDriver().getId().equals(driver.getId())) {
            throw new IllegalArgumentException("Ride not found");
        }
        return bookingRepository.findByRideId(rideId).stream()
                .map(b -> toResponse(b, driver.getId()))
                .toList();
    }

    public List<BookingResponse> getMyBookings(User passenger) {
        return bookingRepository.findByPassengerId(passenger.getId()).stream()
                .map(b -> toResponse(b, passenger.getId()))
                .toList();
    }

    @Transactional
    public BookingResponse rateBooking(User passenger, Long bookingId, RateRequest request) {
        if (passenger.getRole() != Role.USER) {
            throw new AccessDeniedException("Only passengers can rate rides");
        }
        RideBooking booking = bookingRepository.findByIdAndPassengerId(bookingId, passenger.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != BookingStatus.FINISHED) {
            throw new IllegalStateException("You can only rate completed rides");
        }
        if (booking.getRating() != null) {
            throw new IllegalStateException("You have already rated this ride");
        }

        booking.setRating(request.getRating());
        bookingRepository.save(booking);

        // Update driver rolling average
        User driver = booking.getRide().getDriver();
        double newRating = (driver.getRating() * driver.getRatingCount() + request.getRating())
                / (driver.getRatingCount() + 1);
        driver.setRating(newRating);
        driver.setRatingCount(driver.getRatingCount() + 1);
        userRepository.save(driver);

        return toResponse(booking, passenger.getId());
    }

    private RideBooking findBookingForDriver(Long driverId, Long bookingId) {
        return bookingRepository.findByIdAndRideDriverId(bookingId, driverId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public BookingResponse toResponse(RideBooking booking, Long viewerId) {
        boolean isAccepted = booking.getStatus() == BookingStatus.ACCEPTED
                || booking.getStatus() == BookingStatus.IN_RIDE
                || booking.getStatus() == BookingStatus.FINISHED;

        // Phone numbers are only visible after acceptance
        String passengerPhone = isAccepted ? booking.getPassenger().getPhone() : null;
        String driverPhone = isAccepted ? booking.getRide().getDriver().getPhone() : null;

        return BookingResponse.builder()
                .id(booking.getId())
                .rideId(booking.getRide().getId())
                .fromCity(booking.getRide().getFromCity())
                .toCity(booking.getRide().getToCity())
                .passengerId(booking.getPassenger().getId())
                .passengerName(booking.getPassenger().getName())
                .passengerPhone(passengerPhone)
                .driverId(booking.getRide().getDriver().getId())
                .driverName(booking.getRide().getDriver().getName())
                .driverPhone(driverPhone)
                .seats(booking.getSeats())
                .status(booking.getStatus())
                .rating(booking.getRating())
                .build();
    }
}
