package edu.exam_online.exam_online_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Username is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role id is required")
    private Long roleId;

    @Builder.Default
    private Boolean rememberMe = false;
}
