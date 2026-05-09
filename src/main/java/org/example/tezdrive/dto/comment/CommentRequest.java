package org.example.tezdrive.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    @NotBlank(message = "Comment text is required")
    @Size(max = 1000, message = "Comment must be at most 1000 characters")
    private String text;
}
