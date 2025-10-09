package edu.exam_online.exam_online_system.controller;

import com.nimbusds.jose.JOSEException;
import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.dto.request.LoginRequest;
import edu.exam_online.exam_online_system.dto.request.RegisterRequest;
import edu.exam_online.exam_online_system.dto.request.VerifyRegisterRequest;
import edu.exam_online.exam_online_system.dto.response.AuthResponse;
import edu.exam_online.exam_online_system.dto.response.RegisterResponse;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthController {

    AuthService authService;

    @PostMapping("/verify-email")
    public BaseResponse<Boolean> verifyEmail(@RequestBody @Valid VerifyRegisterRequest request) throws AppException {
        return BaseResponse.success(authService.verifyEmail(request));
    }

    @PostMapping("/register")
    public BaseResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) throws AppException {
        return BaseResponse.success(authService.register(request));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(){
        authService.logout();
        return BaseResponse.success();
    }

    @PostMapping("/login")
    public BaseResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) throws AppException, JOSEException {
        return BaseResponse.success(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public BaseResponse<AuthResponse> refreshToken(@RequestBody @Valid String refreshToken) {
        return BaseResponse.success(authService.refresh(refreshToken));
    }

}
