package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.entity.User;
import edu.exam_online.exam_online_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
//
//    @Operation(summary = "Create new user")
//    @PostMapping
//    public BaseResponse<User> createUser(@RequestBody User user) {
//        return BaseResponse.success(userService.createUser(user));
//    }
//
//    @Operation(summary = "Update user by id")
//    @PutMapping("/{id}")
//    public BaseResponse<User> updateUser(@PathVariable Long id, @RequestBody User user) {
//        return BaseResponse.success(userService.updateUser(id, user));
//    }
//
//    @Operation(summary = "Delete user by id")
//    @DeleteMapping("/{id}")
//    public BaseResponse<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return BaseResponse.success();
//    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public BaseResponse<User> getUserById(@PathVariable Long id) {
        return BaseResponse.success(userService.getUserById(id));
    }
//
//    @Operation(summary = "Get user by username")
//    @GetMapping("/username/{username}")
//    public BaseResponse<User> getUserByUsername(@PathVariable String username) {
//        return BaseResponse.success(userService.getUserByUsername(username));
//    }
//
//    @Operation(summary = "Get user by email")
//    @GetMapping("/email/{email}")
//    public BaseResponse<User> getUserByEmail(@PathVariable String email) {
//        return BaseResponse.success(userService.getUserByEmail(email));
//    }
//
//    @Operation(summary = "Get all users")
//    @GetMapping
//    public BaseResponse<List<User>> getAllUsers() {
//        return BaseResponse.success(userService.getAllUsers());
//    }
}
