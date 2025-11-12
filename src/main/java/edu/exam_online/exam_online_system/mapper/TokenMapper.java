package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.entity.auth.Token;
import edu.exam_online.exam_online_system.entity.auth.User;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    default Token toToken(String token, String refreshToken, User user, Long accessTokenExp, Long refreshTokenExp) {
        return Token.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(user)
                .expiresAt(OffsetDateTime.ofInstant(Instant.now().plusMillis(accessTokenExp), ZoneId.systemDefault()))
                .refreshTokenExpiresAt(OffsetDateTime.ofInstant(Instant.now().plusMillis(refreshTokenExp), ZoneId.systemDefault()))
                .build();
    }
}
