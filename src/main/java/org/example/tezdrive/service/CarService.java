package org.example.tezdrive.service;

import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.car.CarRequest;
import org.example.tezdrive.dto.car.CarResponse;
import org.example.tezdrive.entity.Car;
import org.example.tezdrive.entity.CarPhoto;
import org.example.tezdrive.entity.Role;
import org.example.tezdrive.entity.User;
import org.example.tezdrive.exception.AccessDeniedException;
import org.example.tezdrive.repository.CarPhotoRepository;
import org.example.tezdrive.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final CarPhotoRepository carPhotoRepository;
    private final FileStorageService fileStorageService;

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
    public CarResponse uploadPhoto(User driver, Long carId, MultipartFile file) throws IOException {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can upload car photos");
        }
        Car car = carRepository.findByIdAndDriverId(carId, driver.getId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        if (carPhotoRepository.countByCarId(carId) >= 10) {
            throw new IllegalStateException("Maximum 10 photos per car allowed");
        }

        String url = fileStorageService.store(file, "cars");
        carPhotoRepository.save(CarPhoto.builder().car(car).url(url).build());
        return toResponse(car);
    }

    @Transactional
    public void deletePhoto(User driver, Long photoId) {
        CarPhoto photo = carPhotoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Photo not found"));
        if (!photo.getCar().getDriver().getId().equals(driver.getId())) {
            throw new AccessDeniedException("You do not own this photo");
        }
        fileStorageService.delete(photo.getUrl());
        carPhotoRepository.delete(photo);
    }

    @Transactional
    public void deleteCar(User driver, Long carId) {
        if (driver.getRole() != Role.DRIVER) {
            throw new AccessDeniedException("Only drivers can delete cars");
        }
        Car car = carRepository.findByIdAndDriverId(carId, driver.getId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        carPhotoRepository.findByCarId(carId).forEach(p -> fileStorageService.delete(p.getUrl()));
        carRepository.delete(car);
    }

    public Car findByIdAndDriver(Long carId, Long driverId) {
        return carRepository.findByIdAndDriverId(carId, driverId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
    }

    public CarResponse toResponse(Car car) {
        List<String> photos = carPhotoRepository.findByCarId(car.getId()).stream()
                .map(CarPhoto::getUrl)
                .toList();
        return CarResponse.builder()
                .id(car.getId())
                .driverId(car.getDriver().getId())
                .make(car.getMake())
                .model(car.getModel())
                .year(car.getYear())
                .color(car.getColor())
                .plateNumber(car.getPlateNumber())
                .photos(photos)
                .build();
    }
}
