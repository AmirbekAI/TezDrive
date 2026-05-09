package org.example.tezdrive.dto.user;

import lombok.Builder;
import lombok.Data;
import org.example.tezdrive.dto.car.CarResponse;
import org.example.tezdrive.dto.comment.CommentResponse;
import org.example.tezdrive.entity.Role;

import java.util.List;

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
    private String photoUrl;
    private List<CarResponse> cars;
    private List<CommentResponse> comments;
}
