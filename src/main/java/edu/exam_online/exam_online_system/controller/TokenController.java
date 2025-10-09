package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.entity.Token;
import edu.exam_online.exam_online_system.entity.User;
import edu.exam_online.exam_online_system.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Operation(summary = "Find token by value")
    @GetMapping("/{token}")
    public Optional<Token> findByToken(@PathVariable String token) {
        return tokenService.findByToken(token);
    }

    @Operation(summary = "Find token by refresh token")
    @GetMapping("/refresh/{refreshToken}")
    public Optional<Token> findByRefreshToken(@PathVariable String refreshToken) {
        return tokenService.findByRefreshToken(refreshToken);
    }

    @Operation(summary = "Revoke token")
    @DeleteMapping("/{token}")
    public void revokeToken(@PathVariable String token) {
        tokenService.revokeToken(token);
    }

    @Operation(summary = "Revoke all tokens of user")
    @DeleteMapping("/user/{userId}")
    public void revokeAllTokens(@PathVariable Long userId) {
        tokenService.revokeAllUserTokens(userId);
    }
}
