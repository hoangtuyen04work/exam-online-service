package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.entity.UserRole;
import edu.exam_online.exam_online_system.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Operation(summary = "Assign role to user")
    @PostMapping("/{userId}/roles/{roleId}")
    public UserRole assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return userRoleService.assignRoleToUser(userId, roleId);
    }

    @Operation(summary = "Remove role from user")
    @DeleteMapping("/{userId}/roles/{roleId}")
    public void removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userRoleService.removeRoleFromUser(userId, roleId);
    }

    @Operation(summary = "Get roles by user id")
    @GetMapping("/{userId}")
    public List<UserRole> getRolesByUserId(@PathVariable Long userId) {
        return userRoleService.getRolesByUserId(userId);
    }
}
