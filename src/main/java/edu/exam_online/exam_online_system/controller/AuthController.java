package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.dto.request.auth.ChangePasswordRequest;
import edu.exam_online.exam_online_system.dto.request.auth.ForgotPasswordRequest;
import edu.exam_online.exam_online_system.dto.request.auth.LoginRequest;
import edu.exam_online.exam_online_system.dto.request.auth.RefreshTokenRequest;
import edu.exam_online.exam_online_system.dto.request.auth.RegisterRequest;
import edu.exam_online.exam_online_system.dto.request.auth.ResendCodeRequest;
import edu.exam_online.exam_online_system.dto.request.auth.ResetPasswordRequest;
import edu.exam_online.exam_online_system.dto.request.auth.VerifyRegisterRequest;
import edu.exam_online.exam_online_system.dto.response.auth.AuthResponse;
import edu.exam_online.exam_online_system.dto.response.auth.RegisterResponse;
import edu.exam_online.exam_online_system.dto.response.auth.UserInfoResponse;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthController {

    AuthService authService;

    @GetMapping("/me")
    public BaseResponse<UserInfoResponse> me() {
        return BaseResponse.success(authService.getCurrentUserInfo());
    }

    @PostMapping("/verify-email")
    public BaseResponse<Boolean> verifyEmail(@RequestBody @Valid VerifyRegisterRequest request) throws AppException {
        return BaseResponse.success(authService.verifyEmail(request));
    }

    @PostMapping("/register")
    public BaseResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        return BaseResponse.success(authService.register(request));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout() {
        authService.logout();
        return BaseResponse.success();
    }

    @PostMapping("/login")
    public BaseResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return BaseResponse.success(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public BaseResponse<AuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return BaseResponse.success(authService.refresh(request));
    }

    @PutMapping("/change-password")
    public BaseResponse<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);
        return BaseResponse.success();
    }

    @PostMapping("/forgot-password")
    public BaseResponse<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return BaseResponse.success();
    }

    @PostMapping("/reset-password")
    public BaseResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return BaseResponse.success();
    }

    @PostMapping("/resend-code")
    public BaseResponse<Void> resendCode(@RequestBody @Valid ResendCodeRequest request) {
        authService.resendVerificationCode(request);
        return BaseResponse.success();
    }
}
