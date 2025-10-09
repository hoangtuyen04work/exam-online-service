package edu.exam_online.exam_online_system.service.impl;

import edu.exam_online.exam_online_system.entity.Permission;
import edu.exam_online.exam_online_system.entity.Role;
import edu.exam_online.exam_online_system.entity.RolePermission;
import edu.exam_online.exam_online_system.repository.RolePermissionRepository;
import edu.exam_online.exam_online_system.repository.RoleRepository;
import edu.exam_online.exam_online_system.repository.PermissionRepository;
import edu.exam_online.exam_online_system.service.RolePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository,
                                     RoleRepository roleRepository,
                                     PermissionRepository permissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RolePermission assignPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));

        // Kiểm tra đã tồn tại chưa
        if (rolePermissionRepository.existsByRoleAndPermission(role, permission)) {
            throw new RuntimeException("Permission already assigned to role");
        }

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        return rolePermissionRepository.save(rolePermission);
    }

    @Override
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));

        RolePermission rolePermission = rolePermissionRepository.findByRoleAndPermission(role, permission)
                .orElseThrow(() -> new RuntimeException("RolePermission not found"));

        rolePermissionRepository.delete(rolePermission);
    }

    @Override
    public List<RolePermission> getPermissionsByRoleId(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        return rolePermissionRepository.findByRole(role);
    }
}