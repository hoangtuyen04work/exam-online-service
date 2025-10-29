package edu.exam_online.exam_online_system.service.auth;


import edu.exam_online.exam_online_system.entity.auth.Token;
import edu.exam_online.exam_online_system.entity.auth.User;

import java.util.Optional;

public interface TokenService {
    Token saveToken(User user);

    Token saveToken(String token, String refreshToken, User user, Long accessTokenExp, Long refreshTokenExp);
    Optional<Token> findByToken(String token);
    Optional<Token> findByRefreshToken(String refreshToken);
    void revokeToken(String token);
    void revokeAllUserTokens(Long userId);
}
