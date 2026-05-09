package org.example.tezdrive.dto.user;

import lombok.Builder;
import lombok.Data;
import org.example.tezdrive.entity.Role;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String phone;
    private double rating;
    private int ratingCount;
}
