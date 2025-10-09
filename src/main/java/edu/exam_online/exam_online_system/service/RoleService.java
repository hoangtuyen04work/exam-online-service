package edu.exam_online.exam_online_system.service;

import edu.exam_online.exam_online_system.entity.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    Role getRoleById(Long id);
    List<Role> getAllRoles();
}