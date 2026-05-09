package org.example.tezdrive.service;

import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.user.UpdateProfileRequest;
import org.example.tezdrive.dto.user.UserProfileResponse;
import org.example.tezdrive.entity.User;
import org.example.tezdrive.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final CarService carService;
    private final CommentService commentService;
    private final FileStorageService fileStorageService;

    public UserProfileResponse getProfile(User user) {
        return toResponse(user);
    }

    public UserProfileResponse getDriverProfile(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        return toResponse(driver);
    }

    @Transactional
    public UserProfileResponse updateProfile(User user, UpdateProfileRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            user.setPhone(request.getPhone());
        }
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserProfileResponse uploadProfilePhoto(User user, MultipartFile file) throws IOException {
        // delete old photo if exists
        fileStorageService.delete(user.getPhotoUrl());
        String url = fileStorageService.store(file, "avatars");
        user.setPhotoUrl(url);
        return toResponse(userRepository.save(user));
    }

    private UserProfileResponse toResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .phone(user.getPhone())
                .rating(user.getRating())
                .ratingCount(user.getRatingCount())
                .photoUrl(user.getPhotoUrl())
                .cars(carService.getDriverCars(user.getId()))
                .comments(commentService.getCommentsForDriver(user.getId()))
                .build();
    }
}
