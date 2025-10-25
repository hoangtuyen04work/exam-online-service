package edu.exam_online.exam_online_system.service;

import com.nimbusds.jose.JOSEException;
import edu.exam_online.exam_online_system.dto.request.ChangePasswordRequest;
import edu.exam_online.exam_online_system.dto.request.RefreshTokenRequest;
import edu.exam_online.exam_online_system.dto.request.RegisterRequest;
import edu.exam_online.exam_online_system.dto.request.VerifyRegisterRequest;
import edu.exam_online.exam_online_system.dto.response.AuthResponse;
import edu.exam_online.exam_online_system.dto.request.LoginRequest;
import edu.exam_online.exam_online_system.dto.response.RegisterResponse;
import edu.exam_online.exam_online_system.exception.AppException;

public interface AuthService {
    void changePassword(ChangePasswordRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout();

    RegisterResponse register(RegisterRequest request);

    boolean verifyEmail(VerifyRegisterRequest request);
}