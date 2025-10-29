package edu.exam_online.exam_online_system.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleResponse {
    Long roleId;
    String roleName;
    String description;
}
