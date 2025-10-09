package edu.exam_online.exam_online_system.service.impl;

import com.nimbusds.jose.JOSEException;
import edu.exam_online.exam_online_system.entity.Token;
import edu.exam_online.exam_online_system.entity.User;
import edu.exam_online.exam_online_system.mapper.TokenMapper;
import edu.exam_online.exam_online_system.repository.TokenRepository;
import edu.exam_online.exam_online_system.service.TokenService;
import edu.exam_online.exam_online_system.utils.TokenUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static edu.exam_online.exam_online_system.commons.constant.TimeConstant.JWT_EXPIRATION_MS;
import static edu.exam_online.exam_online_system.commons.constant.TimeConstant.REFRESH_TOKEN_EXPIRATION_MS;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    TokenUtils tokenUtils;

    TokenRepository tokenRepository;
    TokenMapper tokenMapper;


    @Override
    public Token saveToken(User user) {
        String accessToken = tokenUtils.generateToken(user);
        String refreshToken = tokenUtils.generateRefreshToken();

        return saveToken(accessToken, refreshToken, user,
                JWT_EXPIRATION_MS,
                REFRESH_TOKEN_EXPIRATION_MS);
    }

    @Override
    public Token saveToken(String token, String refreshToken, User user, Long accessTokenExp, Long refreshTokenExp) {

        Token newToken = tokenMapper.toToken(token, refreshToken, user, accessTokenExp, refreshTokenExp);
        return tokenRepository.save(newToken);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public Optional<Token> findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public void revokeToken(String token) {
        tokenRepository.findByToken(token).ifPresent(t -> {
            t.setIsRevoked(true);
            tokenRepository.delete(t);
        });
    }

    @Override
    public void revokeAllUserTokens(Long userId) {
        tokenRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(userId) && !t.getIsRevoked())
                .forEach(t -> {
                    t.setIsRevoked(true);
                    tokenRepository.save(t);
                });
    }
}
