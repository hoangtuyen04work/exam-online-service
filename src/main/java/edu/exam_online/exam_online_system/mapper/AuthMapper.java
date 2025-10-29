package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.response.auth.AuthResponse;
import edu.exam_online.exam_online_system.dto.response.auth.RegisterResponse;
import edu.exam_online.exam_online_system.entity.auth.Token;
import edu.exam_online.exam_online_system.entity.auth.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    RegisterResponse toRegisterResponse(Long userId);

    default AuthResponse toAuthResponse(Token t, User u){
        return AuthResponse.builder()
                .email(u.getEmail())
                .username(u.getUsername())
                .token(t.getToken())
                .refreshToken(t.getRefreshToken())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .phone(u.getPhone())
                .expiresAt(t.getExpiresAt())
                .refreshTokenExpiresAt(t.getRefreshTokenExpiresAt())
                .build();
    }

    AuthResponse toAuthResponse(Token t);

    AuthResponse toAuthResponse(String token, String refreshToken);
}
