package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.request.auth.RegisterRequest;
import edu.exam_online.exam_online_system.entity.auth.Role;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.utils.TimeUtils;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default User toEntity(String email , String username, Role role, String userCode){
        return User.builder()
                .email(email)
                .username(username)
                .password("password")
                .isActive(true)
                .isEmailVerified(true)
                .createdAt(TimeUtils.getCurrentTime())
                .role(role)
                .userCode(userCode)
                .build();
    }

    default User toEntity(String email , String username, Role role){
        return User.builder()
                .email(email)
                .username(username)
                .role(role)
                .password("password")
                .isActive(true)
                .isEmailVerified(true)
                .build();
    }

    default User toEntity(RegisterRequest request, String username, String password, Role role, String userCode){
        return User.builder()
                .email(request.getEmail())
                .username(username)
                .password(password)
                .isActive(true)
                .isEmailVerified(false)
                .createdAt(TimeUtils.getCurrentTime())
                .role(role)
                .userCode(userCode)
                .build();
    }

}
