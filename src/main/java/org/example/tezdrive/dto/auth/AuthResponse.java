package org.example.tezdrive.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.tezdrive.entity.Role;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String name;
    private String email;
    private Role role;
}
