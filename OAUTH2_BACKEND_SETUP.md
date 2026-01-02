# Backend Configuration - OAuth2 (Azure & Google)

## 🚨 Vấn đề hiện tại

1. ❌ Endpoint `/oauth2/authorization/azure` trả về 401 "Not authenticated"
2. ❌ SecurityConfig chưa cấu hình OAuth2 Login properly
3. ❌ Chưa có Success Handler để redirect về frontend với token
4. ⚠️ Google OAuth2 chưa được cấu hình

## ✅ Giải pháp

### 1. Cập nhật `application.yaml`

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          # Azure AD (Microsoft)
          azure:
            client-id: ${AZURE_CLIENT_ID}
            client-secret: ${AZURE_CLIENT_SECRET}
            scope:
              - openid
              - profile
              - email
              - User.Read
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            authorization-grant-type: authorization_code
            client-name: Microsoft
          
          # Google
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope:
              - openid
              - profile
              - email
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            authorization-grant-type: authorization_code
            client-name: Google
            
        provider:
          azure:
            issuer-uri: https://login.microsoftonline.com/${AZURE_TENANT_ID}/v2.0
          google:
            issuer-uri: https://accounts.google.com

# Frontend URL for redirect after successful OAuth login
frontend:
  url: ${FRONTEND_URL:http://localhost:3000}
```

### 2. Environment Variables (`.env` hoặc system env)

```bash
# Azure AD
AZURE_CLIENT_ID=your-azure-client-id
AZURE_CLIENT_SECRET=your-azure-client-secret
AZURE_TENANT_ID=your-azure-tenant-id

# Google OAuth2
GOOGLE_CLIENT_ID=your-google-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=your-google-client-secret

# Frontend
FRONTEND_URL=http://localhost:3000

# JWT
JWT_SECRET=your-jwt-secret-key
JWT_EXPIRATION=86400000
```

### 3. Tạo `OAuth2AuthenticationSuccessHandler.java`

```java
package edu.exam_online.exam_online_system.config;

import edu.exam_online.exam_online_system.entity.User;
import edu.exam_online.exam_online_system.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException {
        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            
            // Get email from OAuth2 user
            String email = oauth2User.getAttribute("email");
            if (email == null || email.isEmpty()) {
                log.error("Email not found in OAuth2 user attributes");
                redirectToFrontendWithError(response, "Email not found");
                return;
            }

            // Get roleId from request parameter (sent from frontend)
            String roleIdParam = request.getParameter("role");
            Long roleId = roleIdParam != null ? Long.parseLong(roleIdParam) : 2L; // Default: Student

            // Get provider (azure or google)
            String registrationId = (String) request.getAttribute("registrationId");
            if (registrationId == null) {
                registrationId = extractProviderFromRequest(request);
            }

            log.info("OAuth2 login successful - Email: {}, Provider: {}, Role: {}", 
                     email, registrationId, roleId);

            // Create or update user
            User user = authService.findOrCreateOAuthUser(email, oauth2User, roleId, registrationId);

            // Generate JWT token
            String token = authService.generateTokenForUser(user);

            // Redirect to frontend with token
            String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                    .path("/auth/oauth2/callback")
                    .queryParam("token", token)
                    .queryParam("role", roleId)
                    .build()
                    .toUriString();

            log.info("Redirecting to: {}", redirectUrl);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            log.error("Error during OAuth2 authentication success handling", e);
            redirectToFrontendWithError(response, "Authentication failed");
        }
    }

    private void redirectToFrontendWithError(HttpServletResponse response, String errorMessage) throws IOException {
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                .path("/login")
                .queryParam("error", errorMessage)
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(null, response, redirectUrl);
    }

    private String extractProviderFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("/azure")) return "azure";
        if (uri.contains("/google")) return "google";
        return "unknown";
    }
}
```

### 4. Tạo `OAuth2AuthenticationFailureHandler.java`

```java
package edu.exam_online.exam_online_system.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                       HttpServletResponse response,
                                       AuthenticationException exception) throws IOException {
        log.error("OAuth2 authentication failed", exception);

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                .path("/login")
                .queryParam("error", "OAuth authentication failed")
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
```

### 5. Cập nhật `SecurityConfig.java`

```java
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomJwtDecoder customJwtDecoder;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;
    private final OAuth2AuthenticationFailureHandler oauth2FailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/actuator",
                                "/actuator/**",
                                "/api/auth/login",
                                "/api/auth/refresh-token",
                                "/api/auth/verify-email",
                                "/api/auth/register",
                                "/api/roles",
                                "/oauth2/**",           // ✅ OAuth2 endpoints
                                "/login/oauth2/**"      // ✅ OAuth2 login endpoints
                        ).permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                // ✅ Add OAuth2 Login configuration
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oauth2SuccessHandler)
                        .failureHandler(oauth2FailureHandler)
                )
                // JWT authentication for API requests
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomJwtAuthenticationConverter());
        return converter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
```

### 6. Thêm methods vào `AuthService.java`

```java
public interface AuthService {
    // Existing methods...
    
    User findOrCreateOAuthUser(String email, OAuth2User oauth2User, Long roleId, String provider);
    String generateTokenForUser(User user);
}
```

### 7. Implement trong `AuthServiceImpl.java`

```java
@Override
public User findOrCreateOAuthUser(String email, OAuth2User oauth2User, Long roleId, String provider) {
    Optional<User> existingUser = userRepository.findByEmail(email);
    
    if (existingUser.isPresent()) {
        User user = existingUser.get();
        // Update last login time, provider info if needed
        return user;
    }

    // Create new user
    User newUser = new User();
    newUser.setEmail(email);
    newUser.setName(oauth2User.getAttribute("name"));
    newUser.setProvider(provider); // "azure" or "google"
    newUser.setEnabled(true);
    newUser.setEmailVerified(true); // OAuth users are pre-verified

    // Assign role
    Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    newUser.setRoles(Set.of(role));

    return userRepository.save(newUser);
}

@Override
public String generateTokenForUser(User user) {
    return jwtTokenProvider.generateToken(user);
}
```

### 8. Cập nhật `User.java` entity (nếu cần)

```java
@Entity
@Table(name = "users")
public class User {
    // ... existing fields ...
    
    @Column(name = "provider")
    private String provider; // "local", "azure", "google"
    
    @Column(name = "email_verified")
    private Boolean emailVerified = false;
    
    // ... getters/setters ...
}
```

## 🔐 Google Cloud Console Setup

### Tạo OAuth2 Credentials:

1. Vào [Google Cloud Console](https://console.cloud.google.com)
2. Tạo project mới hoặc chọn project hiện tại
3. Enable **Google+ API**
4. Vào **Credentials** → **Create Credentials** → **OAuth 2.0 Client ID**
5. **Application type**: Web application
6. **Authorized redirect URIs**:
   ```
   http://localhost:8888/exam-online-system/login/oauth2/code/google
   http://localhost:3000/auth/oauth2/callback
   ```
7. Copy **Client ID** và **Client Secret**

## 🧪 Testing

### Test Azure Login:
```
http://localhost:8888/exam-online-system/oauth2/authorization/azure?role=2
```

### Test Google Login:
```
http://localhost:8888/exam-online-system/oauth2/authorization/google?role=1
```

## 📝 Migration Script (if needed)

```sql
-- Add provider column to users table
ALTER TABLE users 
ADD COLUMN provider VARCHAR(20) DEFAULT 'local',
ADD COLUMN email_verified BOOLEAN DEFAULT FALSE;

-- Update existing users
UPDATE users SET provider = 'local', email_verified = TRUE WHERE provider IS NULL;
```

## ✅ Checklist

Backend:
- [ ] Cập nhật `application.yaml` với Google config
- [ ] Thêm environment variables (GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET)
- [ ] Tạo `OAuth2AuthenticationSuccessHandler`
- [ ] Tạo `OAuth2AuthenticationFailureHandler`
- [ ] Cập nhật `SecurityConfig` với `.oauth2Login()`
- [ ] Implement `findOrCreateOAuthUser()` trong AuthService
- [ ] Implement `generateTokenForUser()` trong AuthService
- [ ] Update User entity với provider field
- [ ] Test Azure OAuth flow
- [ ] Test Google OAuth flow

Frontend:
- [x] Sửa URL OAuth (dùng VITE_SERVER_PORT_EXPOSE)
- [x] Thêm nút Google login
- [x] Update OAuthCallback component
- [x] Add routes for OAuth callback

## 🎯 Expected Flow

1. User click "Đăng nhập với Microsoft/Google"
2. Frontend redirect: `/oauth2/authorization/{provider}?role={roleId}`
3. Backend redirect to OAuth provider (Azure/Google)
4. User login with OAuth credentials
5. OAuth provider redirect back: `/login/oauth2/code/{provider}?code=...`
6. Backend handle OAuth callback → create/get user → generate JWT
7. Backend redirect to frontend: `/auth/oauth2/callback?token=...&role=...`
8. Frontend save token → redirect to dashboard
