package org.example.tezdrive.repository;

import org.example.tezdrive.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByDriverId(Long driverId);
    Optional<Car> findByIdAndDriverId(Long id, Long driverId);
    boolean existsByPlateNumber(String plateNumber);
}
