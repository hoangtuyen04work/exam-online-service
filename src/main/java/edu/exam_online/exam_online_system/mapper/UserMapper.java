package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.request.RegisterRequest;
import edu.exam_online.exam_online_system.entity.Role;
import edu.exam_online.exam_online_system.entity.User;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default User toEntity(String email , String username, Role role, String userCode){
        return User.builder()
                .email(email)
                .username(username)
                .password("password")
                .isActive(true)
                .isEmailVerified(true)
                .createdAt(LocalDateTime.now())
                .role(role)
                .userCode(userCode)
                .build();
    }

    default User toEntity(RegisterRequest request, String username, String password, Role role, String userCode){
        return User.builder()
                .email(request.getEmail())
                .username(username)
                .password(password)
                .isActive(true)
                .isEmailVerified(false)
                .createdAt(LocalDateTime.now())
                .role(role)
                .userCode(userCode)
                .build();
    }

}
