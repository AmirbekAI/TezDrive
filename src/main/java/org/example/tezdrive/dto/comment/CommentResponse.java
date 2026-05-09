package org.example.tezdrive.dto.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private Long bookingId;
    private Long passengerId;
    private String passengerName;
    private String text;
    private LocalDateTime createdAt;
}
