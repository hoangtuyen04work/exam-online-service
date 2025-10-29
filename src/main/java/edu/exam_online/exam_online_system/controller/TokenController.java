//package edu.exam_online.exam_online_system.controller;
//
//import edu.exam_online.exam_online_system.service.auth.TokenService;
//import io.swagger.v3.oas.annotations.Operation;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/tokens")
//public class TokenController {
//
//    private final TokenService tokenService;
//
//    public TokenController(TokenService tokenService) {
//        this.tokenService = tokenService;
//    }
//
//    @Operation(summary = "Revoke token")
//    @DeleteMapping("/{token}")
//    public void revokeToken(@PathVariable String token) {
//        tokenService.revokeToken(token);
//    }
//
//    @Operation(summary = "Revoke all tokens of user")
//    @DeleteMapping("/user/{userId}")
//    public void revokeAllTokens(@PathVariable Long userId) {
//        tokenService.revokeAllUserTokens(userId);
//    }
//}
