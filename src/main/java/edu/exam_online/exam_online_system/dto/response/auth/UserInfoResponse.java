package edu.exam_online.exam_online_system.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private Long roleId;
    private OffsetDateTime lastLogin;
    private Set<String> roles;
    private Set<String> permissions;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
