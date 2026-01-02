package edu.exam_online.exam_online_system.dto.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResendCodeRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
}
