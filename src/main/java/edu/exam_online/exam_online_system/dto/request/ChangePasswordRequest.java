package edu.exam_online.exam_online_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Old password is required")
    private String oldPassword;
    @NotBlank(message = "Old password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;
}
