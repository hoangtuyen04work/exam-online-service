package edu.exam_online.exam_online_system.repository.auth;

import edu.exam_online.exam_online_system.entity.auth.Permission;
import edu.exam_online.exam_online_system.entity.auth.Role;
import edu.exam_online.exam_online_system.entity.auth.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);
    List<RolePermission> findByRole(Role role);
    boolean existsByRoleAndPermission(Role role, Permission permission);
}
