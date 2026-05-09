package org.example.tezdrive.service;

import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.ride.CreateRideRequest;
import org.example.tezdrive.dto.ride.RideOfferResponse;
import org.example.tezdrive.dto.ride.RideSearchRequest;
import org.example.tezdrive.entity.RideOffer;
import org.example.tezdrive.entity.RideStatus;
import org.example.tezdrive.entity.Role;
import org.example.tezdrive.entity.User;
import org.example.tezdrive.exception.AccessDeniedException;
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
