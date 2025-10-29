package edu.exam_online.exam_online_system.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Tiện ích giúp truy cập thông tin từ Spring Security context và JWT token hiện tại.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    /**
     * Lấy Authentication hiện tại (JwtAuthenticationToken, UsernamePasswordAuthenticationToken,...)
     */
    public static Authentication getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .orElse(null);
    }

    /**
     * Ép kiểu Authentication sang class mong muốn (nếu hợp lệ).
     */
    public static <T extends Authentication> T getAuthentication(Class<T> clazz) {
        Authentication authentication = getAuthentication();
        return clazz.isInstance(authentication) ? clazz.cast(authentication) : null;
    }

    /**
     * Lấy toàn bộ claim của JWT hiện tại.
     */
    public static Map<String, Object> getJwtClaims() {
        return Optional.ofNullable(getAuthentication(JwtAuthenticationToken.class))
                .map(JwtAuthenticationToken::getTokenAttributes)
                .orElse(Map.of());
    }

    /**
     * Lấy giá trị claim cụ thể (theo key).
     */
    public static String getJwtClaim(String claimName) {
        return Optional.ofNullable(getJwtClaims().get(claimName))
                .map(Object::toString)
                .orElse(null);
    }

    // ===========================================================
    // 🔹 Các helper method dành riêng cho token của bạn
    // ===========================================================

    /**
     * Lấy "sub" (subject, thường là email hoặc username).
     */
    public static Long getUserId() {
        return Long.parseLong(getJwtClaim("sub"));
    }

    /**
     * Lấy danh sách role từ claim "roles".
     */
    @SuppressWarnings("unchecked")
    public static List<String> getRoles() {
        Object roles = getJwtClaims().get("roles");
        if (roles instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }
        return List.of();
    }

    /**
     * Lấy thời điểm token được cấp (claim "iat").
     */
    public static LocalDateTime getIssuedAt() {
        Object value = getJwtClaims().get("iat");
        if (value instanceof Number num) {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(num.longValue()), ZoneOffset.UTC);
        }
        return null;
    }

    /**
     * Lấy thời điểm token hết hạn (claim "exp").
     */
    public static LocalDateTime getExpiresAt() {
        Object value = getJwtClaims().get("exp");
        if (value instanceof Number num) {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(num.longValue()), ZoneOffset.UTC);
        }
        return null;
    }

    /**
     * Kiểm tra token đã hết hạn chưa.
     */
    public static boolean isTokenExpired() {
        LocalDateTime exp = getExpiresAt();
        return exp != null && exp.isBefore(LocalDateTime.now(ZoneOffset.UTC));
    }
}
