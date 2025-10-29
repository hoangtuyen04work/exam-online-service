package edu.exam_online.exam_online_system.service.auth;

import edu.exam_online.exam_online_system.entity.auth.Permission;

import java.util.List;

public interface PermissionService {
    Permission createPermission(Permission permission);
    Permission updatePermission(Long id, Permission permission);
    void deletePermission(Long id);
    Permission getPermissionById(Long id);
    List<Permission> getAllPermissions();
}
