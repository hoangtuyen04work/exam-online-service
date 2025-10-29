package edu.exam_online.exam_online_system.service.auth;

import edu.exam_online.exam_online_system.dto.request.auth.ChangePasswordRequest;
import edu.exam_online.exam_online_system.dto.request.auth.RefreshTokenRequest;
import edu.exam_online.exam_online_system.dto.request.auth.RegisterRequest;
import edu.exam_online.exam_online_system.dto.request.auth.VerifyRegisterRequest;
import edu.exam_online.exam_online_system.dto.response.auth.AuthResponse;
import edu.exam_online.exam_online_system.dto.request.auth.LoginRequest;
import edu.exam_online.exam_online_system.dto.response.auth.RegisterResponse;

public interface AuthService {
    void changePassword(ChangePasswordRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout();

    RegisterResponse register(RegisterRequest request);

    boolean verifyEmail(VerifyRegisterRequest request);
}