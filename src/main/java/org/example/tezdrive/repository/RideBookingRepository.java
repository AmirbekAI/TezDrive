package org.example.tezdrive.repository;

import org.example.tezdrive.entity.BookingStatus;
import org.example.tezdrive.entity.RideBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideBookingRepository extends JpaRepository<RideBooking, Long> {

    List<RideBooking> findByRideId(Long rideId);

    List<RideBooking> findByPassengerId(Long passengerId);

    List<RideBooking> findByRideIdAndStatus(Long rideId, BookingStatus status);

    Optional<RideBooking> findByIdAndPassengerId(Long id, Long passengerId);

    Optional<RideBooking> findByIdAndRideDriverId(Long id, Long driverId);

    boolean existsByRideIdAndPassengerIdAndStatusIn(Long rideId, Long passengerId, List<BookingStatus> statuses);
}
