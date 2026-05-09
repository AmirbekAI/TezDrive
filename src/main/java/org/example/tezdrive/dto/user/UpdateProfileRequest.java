package org.example.tezdrive.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    private String name;
    private String phone;
}
