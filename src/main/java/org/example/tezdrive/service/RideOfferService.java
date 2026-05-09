package org.example.tezdrive.service;

import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.ride.CreateRideRequest;
import org.example.tezdrive.dto.ride.RideOfferResponse;
import org.example.tezdrive.dto.ride.RideSearchRequest;
import org.example.tezdrive.entity.BookingStatus;
import org.example.tezdrive.entity.RideBooking;
import org.example.tezdrive.entity.RideOffer;
import org.example.tezdrive.entity.RideStatus;
import org.example.tezdrive.entity.Role;
import org.example.tezdrive.entity.User;
import org.example.tezdrive.exception.AccessDeniedException;
import org.example.tezdrive.repository.RideBookingRepository;
import org.example.tezdrive.repository.RideOfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RideOfferService {

    private final RideOfferRepository rideOfferRepository;
    private final RideBookingRepository bookingRepository;

    @Transactional
    public RideOfferResponse create(User driver, CreateRideRequest request) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can create ride offers");
        }
        RideOffer ride = RideOffer.builder()
                .driver(driver)
                .fromCity(request.getFromCity())
                .toCity(request.getToCity())
                .departureTime(request.getDepartureTime())
                .availableSeats(request.getAvailableSeats())
                .pricePerSeat(request.getPricePerSeat())
                .notes(request.getNotes())
                .status(RideStatus.ACTIVE)
                .build();

        return toResponse(rideOfferRepository.save(ride));
    }

    public List<RideOfferResponse> getMyRides(User driver) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can view their ride offers");
        }
        return rideOfferRepository.findByDriverIdOrderByDepartureTimeDesc(driver.getId())
                .stream().map(this::toResponse).toList();
    }

    public List<RideOfferResponse> search(RideSearchRequest request) {
        LocalDateTime dateFrom = request.getDate().atStartOfDay();
        LocalDateTime dateTo = request.getDate().atTime(LocalTime.MAX);

        return rideOfferRepository.search(
                        request.getFromCity(),
                        request.getToCity(),
                        dateFrom,
                        dateTo,
                        request.getMinSeats(),
                        request.getMaxPrice()
                )
                .stream().map(this::toResponse).toList();
    }

    public RideOfferResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public RideOffer findById(Long id) {
        return rideOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + id));
    }

    @Transactional
    public RideOfferResponse startRide(User driver, Long rideId) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can start rides");
        }
        RideOffer ride = findById(rideId);
        if (!ride.getDriver().getId().equals(driver.getId())) {
            throw new IllegalArgumentException("Ride not found");
        }
        if (ride.getStatus() != RideStatus.ACTIVE) {
            throw new IllegalStateException("Ride is not in ACTIVE status");
        }
        List<RideBooking> acceptedBookings = bookingRepository.findByRideIdAndStatus(rideId, BookingStatus.ACCEPTED);
        if (acceptedBookings.isEmpty()) {
            throw new IllegalStateException("Cannot start a ride with no accepted bookings");
        }
        ride.setStatus(RideStatus.STARTED);
        acceptedBookings.forEach(b -> b.setStatus(BookingStatus.IN_RIDE));
        bookingRepository.saveAll(acceptedBookings);
        return toResponse(rideOfferRepository.save(ride));
    }

    @Transactional
    public RideOfferResponse finishRide(User driver, Long rideId) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can finish rides");
        }
        RideOffer ride = findById(rideId);
        if (!ride.getDriver().getId().equals(driver.getId())) {
            throw new IllegalArgumentException("Ride not found");
        }
        if (ride.getStatus() != RideStatus.STARTED) {
            throw new IllegalStateException("Ride is not in STARTED status");
        }
        ride.setStatus(RideStatus.COMPLETED);
        List<RideBooking> inRideBookings = bookingRepository.findByRideIdAndStatus(rideId, BookingStatus.IN_RIDE);
        inRideBookings.forEach(b -> b.setStatus(BookingStatus.FINISHED));
        bookingRepository.saveAll(inRideBookings);
        return toResponse(rideOfferRepository.save(ride));
    }

    public RideOfferResponse toResponse(RideOffer ride) {
        return RideOfferResponse.builder()
                .id(ride.getId())
                .driverId(ride.getDriver().getId())
                .driverName(ride.getDriver().getName())
                .driverRating(ride.getDriver().getRating())
                .driverRatingCount(ride.getDriver().getRatingCount())
                .fromCity(ride.getFromCity())
                .toCity(ride.getToCity())
                .departureTime(ride.getDepartureTime())
                .availableSeats(ride.getAvailableSeats())
                .pricePerSeat(ride.getPricePerSeat())
                .notes(ride.getNotes())
                .status(ride.getStatus())
                .build();
    }
}
