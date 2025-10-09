package edu.exam_online.exam_online_system.service;

import edu.exam_online.exam_online_system.entity.RolePermission;

import java.util.List;

public interface RolePermissionService {
    RolePermission assignPermissionToRole(Long roleId, Long permissionId);
    void removePermissionFromRole(Long roleId, Long permissionId);
    List<RolePermission> getPermissionsByRoleId(Long roleId);
}
