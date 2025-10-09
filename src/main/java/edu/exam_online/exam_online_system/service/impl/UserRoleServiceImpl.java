package edu.exam_online.exam_online_system.service.impl;

import edu.exam_online.exam_online_system.entity.Role;
import edu.exam_online.exam_online_system.entity.User;
import edu.exam_online.exam_online_system.entity.UserRole;
import edu.exam_online.exam_online_system.repository.RoleRepository;
import edu.exam_online.exam_online_system.repository.UserRepository;
import edu.exam_online.exam_online_system.repository.UserRoleRepository;
import edu.exam_online.exam_online_system.service.UserRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    UserRoleRepository userRoleRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Override
    @Transactional
    public UserRole assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));

        userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .ifPresent(existing -> {
                    throw new IllegalStateException("Role already assigned to this user");
                });

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        return userRoleRepository.save(userRole);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not assigned to user"));
        userRoleRepository.delete(userRole);
    }

    @Override
    public List<UserRole> getRolesByUserId(Long userId) {
        return userRoleRepository.findByUserId(userId);
    }
}
