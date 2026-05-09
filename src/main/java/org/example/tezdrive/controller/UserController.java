package org.example.tezdrive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.user.UpdateProfileRequest;
import org.example.tezdrive.dto.user.UserProfileResponse;
import org.example.tezdrive.security.UserPrincipal;
import org.example.tezdrive.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ---- My profile ----
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.getProfile(principal.getUser()));
    }

    // ---- Update my profile (name, phone) ----
    @PatchMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMe(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(principal.getUser(), request));
    }

    // ---- View any driver's public profile ----
    @GetMapping("/drivers/{driverId}")
    public ResponseEntity<UserProfileResponse> driverProfile(@PathVariable Long driverId) {
        return ResponseEntity.ok(userService.getDriverProfile(driverId));
    }
}
