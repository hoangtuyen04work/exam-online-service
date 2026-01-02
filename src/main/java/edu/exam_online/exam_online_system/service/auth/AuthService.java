package edu.exam_online.exam_online_system.service.auth;

import edu.exam_online.exam_online_system.dto.request.auth.ChangePasswordRequest;
import edu.exam_online.exam_online_system.dto.request.auth.ForgotPasswordRequest;
import edu.exam_online.exam_online_system.dto.request.auth.RefreshTokenRequest;
import edu.exam_online.exam_online_system.dto.request.auth.RegisterRequest;
import edu.exam_online.exam_online_system.dto.request.auth.ResendCodeRequest;
import edu.exam_online.exam_online_system.dto.request.auth.ResetPasswordRequest;
import edu.exam_online.exam_online_system.dto.request.auth.VerifyRegisterRequest;
import edu.exam_online.exam_online_system.dto.response.auth.AuthResponse;
import edu.exam_online.exam_online_system.dto.request.auth.LoginRequest;
import edu.exam_online.exam_online_system.dto.response.auth.RegisterResponse;
import edu.exam_online.exam_online_system.dto.response.auth.UserInfoResponse;

public interface AuthService {
    void changePassword(ChangePasswordRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout();

    RegisterResponse register(RegisterRequest request);

    boolean verifyEmail(VerifyRegisterRequest request);

    UserInfoResponse getCurrentUserInfo();

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void resendVerificationCode(ResendCodeRequest request);
}