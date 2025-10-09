package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.entity.Token;
import edu.exam_online.exam_online_system.entity.User;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    default Token toToken(String token, String refreshToken, User user, Long accessTokenExp, Long refreshTokenExp) {
        return Token.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(user)
                .expiresAt(LocalDateTime.ofInstant(Instant.now().plusMillis(accessTokenExp), ZoneId.systemDefault()))
                .refreshTokenExpiresAt(LocalDateTime.ofInstant(Instant.now().plusMillis(refreshTokenExp), ZoneId.systemDefault()))
                .build();
    }
}
