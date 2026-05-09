package org.example.tezdrive.repository;

import org.example.tezdrive.entity.DriverComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverCommentRepository extends JpaRepository<DriverComment, Long> {
    List<DriverComment> findByDriverIdOrderByCreatedAtDesc(Long driverId);
    boolean existsByBookingId(Long bookingId);
}
