package org.example.tezdrive.repository;

import org.example.tezdrive.entity.CarPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarPhotoRepository extends JpaRepository<CarPhoto, Long> {
    List<CarPhoto> findByCarId(Long carId);
    long countByCarId(Long carId);
}
