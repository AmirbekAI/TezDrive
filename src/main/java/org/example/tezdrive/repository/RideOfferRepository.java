package org.example.tezdrive.repository;

import org.example.tezdrive.entity.RideOffer;
import org.example.tezdrive.entity.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RideOfferRepository extends JpaRepository<RideOffer, Long> {

    List<RideOffer> findByDriverIdOrderByDepartureTimeDesc(Long driverId);

    List<RideOffer> findByDriverIdAndStatusOrderByDepartureTimeDesc(Long driverId, RideStatus status);

    @Query("""
            SELECT r FROM RideOffer r
            WHERE r.status = 'ACTIVE'
              AND LOWER(r.fromCity) = LOWER(:fromCity)
              AND LOWER(r.toCity) = LOWER(:toCity)
              AND r.departureTime >= :dateFrom
              AND r.departureTime < :dateTo
              AND r.availableSeats >= :minSeats
              AND r.pricePerSeat <= :maxPrice
            ORDER BY r.driver.rating DESC, r.departureTime ASC
            """)
    List<RideOffer> search(
            @Param("fromCity") String fromCity,
            @Param("toCity") String toCity,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("minSeats") int minSeats,
            @Param("maxPrice") BigDecimal maxPrice
    );
}
