package org.example.tezdrive.service;

import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.car.CarRequest;
import org.example.tezdrive.dto.car.CarResponse;
import org.example.tezdrive.entity.Car;
import org.example.tezdrive.entity.Role;
import org.example.tezdrive.entity.User;
import org.example.tezdrive.exception.AccessDeniedException;
import org.example.tezdrive.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;

    public List<CarResponse> getMyCars(User driver) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can manage cars");
        }
        return carRepository.findByDriverId(driver.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CarResponse> getDriverCars(Long driverId) {
        return carRepository.findByDriverId(driverId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CarResponse addCar(User driver, CarRequest request) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can add cars");
        }
        if (carRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new IllegalArgumentException("A car with this plate number already exists");
        }
        Car car = Car.builder()
                .driver(driver)
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .color(request.getColor())
                .plateNumber(request.getPlateNumber())
                .build();
        return toResponse(carRepository.save(car));
    }

    @Transactional
    public void deleteCar(User driver, Long carId) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can delete cars");
        }
        Car car = carRepository.findByIdAndDriverId(carId, driver.getId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        carRepository.delete(car);
    }

    public Car findByIdAndDriver(Long carId, Long driverId) {
        return carRepository.findByIdAndDriverId(carId, driverId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
    }

    public CarResponse toResponse(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .driverId(car.getDriver().getId())
                .make(car.getMake())
                .model(car.getModel())
                .year(car.getYear())
                .color(car.getColor())
                .plateNumber(car.getPlateNumber())
                .build();
    }
}
