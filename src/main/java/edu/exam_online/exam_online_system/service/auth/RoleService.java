package edu.exam_online.exam_online_system.service.auth;

import edu.exam_online.exam_online_system.dto.response.auth.RoleResponse;
import edu.exam_online.exam_online_system.entity.auth.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    Role getRoleById(Long id);
    List<RoleResponse> getAllRoles();
}