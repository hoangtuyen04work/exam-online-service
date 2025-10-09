package edu.exam_online.exam_online_system.service;

import edu.exam_online.exam_online_system.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    UserRole assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);
    List<UserRole> getRolesByUserId(Long userId);
}
