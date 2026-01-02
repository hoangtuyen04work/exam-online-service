package edu.exam_online.exam_online_system.service.auth.impl;

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
import edu.exam_online.exam_online_system.entity.auth.Code;
import edu.exam_online.exam_online_system.entity.auth.Role;
import edu.exam_online.exam_online_system.entity.auth.Token;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.AuthMapper;
import edu.exam_online.exam_online_system.mapper.CodeMapper;
import edu.exam_online.exam_online_system.mapper.UserMapper;
import edu.exam_online.exam_online_system.repository.auth.CodeRepository;
import edu.exam_online.exam_online_system.repository.auth.RoleRepository;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.service.auth.AuthService;
import edu.exam_online.exam_online_system.service.auth.EmailService;
import edu.exam_online.exam_online_system.service.auth.TokenService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import edu.exam_online.exam_online_system.utils.TimeUtils;
import edu.exam_online.exam_online_system.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static edu.exam_online.exam_online_system.commons.constant.TimeConstant.JWT_EXPIRATION_MS;
import static edu.exam_online.exam_online_system.commons.constant.TimeConstant.REFRESH_TOKEN_EXPIRATION_MS;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    TokenUtils tokenUtils;
    TokenService tokenService;
    EmailService emailService;

    RoleRepository roleRepository;
    CodeRepository codeRepository;

    AuthMapper authMapper;
    UserMapper userMapper;
    CodeMapper codeMapper;

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        log.info("changePassword request: {}", request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.CHANGE_PASSWORD_FAILED);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("changePassword success: {}", user.getUsername());
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("login request: {}", request);
        User user = userRepository.findByEmailAndRoleId(request.getEmail(), request.getRoleId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        validateLogin(request, user);

        Token token = tokenService.saveToken(user);

        log.info("login success: {}", user.getUsername());
        return authMapper.toAuthResponse(token, user);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        Token tokenEntity = tokenService.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_INVALID));

        User user = tokenEntity.getUser();

        try {
            String newAccessToken = tokenUtils.generateToken(user);
            String newRefreshToken = tokenUtils.generateRefreshToken();

            tokenService.revokeToken(tokenEntity.getToken());
            tokenService.saveToken(newAccessToken, newRefreshToken, user,
                    JWT_EXPIRATION_MS, REFRESH_TOKEN_EXPIRATION_MS);

            return authMapper.toAuthResponse(newAccessToken, newRefreshToken);
        } catch (Exception e) {
            log.error("Refresh failed", e);
            throw new AppException(ErrorCode.REFRESH_TOKEN_FALSE);
        }
    }

    @Override
    public void logout() {
        String accessToken = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getToken()
                .getTokenValue();

        tokenService.revokeToken(accessToken);
    }

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        log.info("register request: {}", request);

        // Check if user exists
        User existingUser = userRepository.findByEmailAndRoleId(request.getEmail(), request.getRoleId())
                .orElse(null);

        if (existingUser != null) {
            // If email is already verified, user cannot register again
            if (existingUser.getIsEmailVerified()) {
                log.error("User with verified email already exists: {}", request.getEmail());
                throw new AppException(ErrorCode.USER_EXISTED);
            }

            // If email is not verified, allow re-registration by updating user info and
            // sending new code
            log.info("User exists but not verified, allowing re-registration: {}", request.getEmail());
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(existingUser);

            // Generate and send new verification code
            String code = buildCode();
            Code verificationCode = codeMapper.toVerificationCode(code, existingUser);
            codeRepository.save(verificationCode);

            emailService.sendVerificationEmail(existingUser.getEmail(), code);
            log.info("New verification code sent to: {}", existingUser.getEmail());

            return authMapper.toRegisterResponse(existingUser.getId());
        }

        // Create new user
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        String username = getUsername(request.getEmail());
        String code = buildCode();

        User user = userMapper.toEntity(request, username, passwordEncoder.encode(request.getPassword()), role,
                code);
        userRepository.save(user);

        Code verificationCode = codeMapper.toVerificationCode(code, user);
        codeRepository.save(verificationCode);

        emailService.sendVerificationEmail(user.getEmail(), code);

        log.info("email sent: {}", user.getEmail());
        return authMapper.toRegisterResponse(user.getId());
    }

    @Override
    @Transactional
    public boolean verifyEmail(VerifyRegisterRequest request) {
        log.info("verifyEmail userId: {}", request.getUserId());
        Code verificationCode = codeRepository
                .findByCodeAndUserId(request.getCode(), request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.WRONG_VERIFICATION_CODE));

        validateVerifyEmail(request, verificationCode);

        verificationCode.setIsUsed(true);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setIsEmailVerified(true);

        userRepository.save(user);
        codeRepository.save(verificationCode);
        return true;
    }

    private void validateVerifyEmail(VerifyRegisterRequest request, Code verificationCode) {
        if (verificationCode.getExpiresAt().isBefore(TimeUtils.getCurrentTime())) {
            log.error("Verification code expired");
            throw new AppException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (!request.getCode().equals(verificationCode.getCode())) {
            throw new AppException(ErrorCode.WRONG_VERIFICATION_CODE);
        }
    }

    private void validateLogin(LoginRequest request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD_OR_USERID);
        }

        if (user.getIsEmailVerified() == false) {
            throw new AppException(ErrorCode.ACCOUNT_IS_INACTIVE);
        }
    }

    private String buildCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private String getUsername(String email) {
        return email.split("@")[0];
    }

    @Override
    public UserInfoResponse getCurrentUserInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String fullName = (user.getFirstName() != null ? user.getFirstName() : "") +
                (user.getLastName() != null ? " " + user.getLastName() : "");
        fullName = fullName.trim();

        Set<String> roles = Set.of(user.getRole().getName());

        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(fullName.isEmpty() ? user.getUsername() : fullName)
                .phone(user.getPhone())
                .isActive(user.getIsActive())
                .isEmailVerified(user.getIsEmailVerified())
                .roleId(user.getRole().getId())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        log.info("Forgot password request for email: {}", request.getEmail());

        User user = userRepository.findByEmailAndRoleId(request.getEmail(), request.getRoleId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!user.getIsEmailVerified()) {
            throw new AppException(ErrorCode.ACCOUNT_IS_INACTIVE);
        }

        // Generate reset code
        String code = buildCode();
        Code resetCode = codeMapper.toVerificationCode(code, user);
        codeRepository.save(resetCode);

        // Send reset code to email
        emailService.sendVerificationEmail(user.getEmail(), code);
        log.info("Password reset code sent to: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        log.info("Reset password request for email: {}", request.getEmail());

        User user = userRepository.findByEmailAndRoleId(request.getEmail(), request.getRoleId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Code resetCode = codeRepository.findByCodeAndUserId(request.getCode(), user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.WRONG_VERIFICATION_CODE));

        // Validate code
        if (resetCode.getExpiresAt().isBefore(TimeUtils.getCurrentTime())) {
            log.error("Reset code expired");
            throw new AppException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (resetCode.getIsUsed()) {
            throw new AppException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        resetCode.setIsUsed(true);

        userRepository.save(user);
        codeRepository.save(resetCode);

        log.info("Password reset successful for user: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void resendVerificationCode(ResendCodeRequest request) {
        log.info("Resend verification code for userId: {}", request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (user.getIsEmailVerified()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Generate new code
        String code = buildCode();
        Code verificationCode = codeMapper.toVerificationCode(code, user);
        codeRepository.save(verificationCode);

        // Send new code
        emailService.sendVerificationEmail(user.getEmail(), code);
        log.info("Verification code resent to: {}", user.getEmail());
    }
}
