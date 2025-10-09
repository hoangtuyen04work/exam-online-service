# Spring Security 6 Migration Guide

## ✅ Đã cập nhật thành công!

Hệ thống đã được cập nhật để sử dụng Spring Security 6 với SecurityFilterChain thay vì doFilterInternal.

## 🔄 Thay đổi chính

### 1. **SecurityFilterChain thay vì doFilterInternal**

#### ❌ Cũ (Spring Security 5):
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(...) {
        // Custom filter logic
    }
}
```

#### ✅ Mới (Spring Security 6):
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .addFilterBefore(jwtAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class)
        .build();
}
```

### 2. **OAuth2 Configuration**

#### ❌ Cũ:
```java
@Configuration
public class AzureOAuth2Config {
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        // Manual configuration
    }
}
```

#### ✅ Mới:
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .userService(azureUserService())
            )
        )
        .build();
}
```

### 3. **Azure OAuth2 User Service**

#### ✅ Mới:
```java
@Service
public class AzureOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Custom user loading logic
        return new AzureOAuth2User(user, attributes, authorities);
    }
}
```

## 🏗️ Kiến trúc mới

### **SecurityFilterChain Architecture**

```
Request → SecurityFilterChain → Filters → Controllers
                ↓
        [JWT Filter] → [OAuth2 Filter] → [Authentication]
```

### **Filter Order**
1. **CORS Filter** - Cross-origin requests
2. **JWT Authentication Filter** - JWT token validation
3. **OAuth2 Login Filter** - Azure AD authentication
4. **Authorization Filter** - Role-based access control

## 📁 File Structure

### **Configuration Files**
```
src/main/java/edu/exam_online/exam_online_system/config/
├── SecurityConfig.java              # Main security configuration
├── JwtAuthenticationFilter.java    # JWT filter (updated)
├── AzureOAuth2UserService.java     # Azure OAuth2 user service
└── AzureOAuth2User.java           # Custom OAuth2 user implementation
```

### **Properties Configuration**
```
src/main/resources/
└── application.properties          # Spring Security 6 configuration
```

## 🔧 Configuration Details

### **SecurityFilterChain Configuration**

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/oauth2/**").permitAll()
            .requestMatchers("/login/oauth2/code/**").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/oauth2/authorization/azure")
            .defaultSuccessUrl("/api/auth/azure/callback", true)
            .userInfoEndpoint(userInfo -> userInfo
                .userService(azureUserService())
            )
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(jwtAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class)
        .build();
}
```

### **JWT Filter Integration**

```java
@Bean
public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtService, userDetailsService);
}
```

### **Azure OAuth2 User Service**

```java
@Bean
public AzureOAuth2UserService azureUserService() {
    return new AzureOAuth2UserService();
}
```

## 🚀 Lợi ích của Spring Security 6

### 1. **Declarative Configuration**
- Cấu hình rõ ràng và dễ hiểu
- Type-safe configuration
- IDE support tốt hơn

### 2. **Better Filter Management**
- SecurityFilterChain thay vì manual filter
- Automatic filter ordering
- Better integration với OAuth2

### 3. **Enhanced OAuth2 Support**
- Native OAuth2 integration
- Better Azure AD support
- Automatic user mapping

### 4. **Improved Performance**
- Optimized filter chain
- Better caching
- Reduced overhead

## 🔐 Security Features

### **Authentication Methods**
1. **JWT Token Authentication** - Stateless authentication
2. **Azure AD OAuth2** - Single Sign-On
3. **Form-based Login** - Traditional username/password

### **Authorization**
1. **Role-based Access Control** - ADMIN, TEACHER, STUDENT
2. **Permission-based Access** - Granular permissions
3. **Method-level Security** - @PreAuthorize annotations

### **Session Management**
1. **Stateless Sessions** - JWT-based
2. **OAuth2 Sessions** - Azure AD integration
3. **Session Security** - CSRF protection

## 📡 API Endpoints

### **Authentication Endpoints**
```
POST /api/auth/login              # JWT login
POST /api/auth/register           # User registration
GET  /api/auth/me                 # Current user info
POST /api/auth/logout             # Logout
```

### **Azure AD Endpoints**
```
GET  /oauth2/authorization/azure   # Azure login redirect
GET  /api/auth/azure/callback     # Azure callback
GET  /api/auth/azure/user-info     # Azure user info
POST /api/auth/azure/logout        # Azure logout
```

### **Test Endpoints**
```
GET  /api/test/public             # Public endpoint
GET  /api/test/protected          # Protected endpoint
GET  /api/test/user-info          # User info endpoint
```

## 🧪 Testing

### **Test Authentication**

```bash
# Test JWT authentication
curl -X POST http://localhost:8888/exam-online-system/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail": "test@example.com", "password": "password123"}'

# Test Azure AD authentication
curl -X GET http://localhost:8888/exam-online-system/oauth2/authorization/azure

# Test protected endpoint
curl -X GET http://localhost:8888/exam-online-system/api/test/protected \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### **Test OAuth2 Flow**

1. **Redirect to Azure**: `GET /oauth2/authorization/azure`
2. **Azure Callback**: `GET /login/oauth2/code/azure`
3. **User Info**: `GET /api/auth/azure/user-info`

## 🔧 Troubleshooting

### **Common Issues**

1. **Filter Order Issues**
   - Đảm bảo JWT filter được thêm trước OAuth2 filter
   - Kiểm tra SecurityFilterChain configuration

2. **OAuth2 Configuration**
   - Kiểm tra Azure AD app registration
   - Đảm bảo redirect URI đúng
   - Verify client credentials

3. **JWT Token Issues**
   - Kiểm tra JWT secret key
   - Verify token expiration
   - Check token format

### **Debug Logging**

```properties
logging.level.org.springframework.security=DEBUG
logging.level.com.azure.spring=DEBUG
logging.level.edu.exam_online.exam_online_system=DEBUG
```

## 📈 Performance Improvements

### **Before (Spring Security 5)**
- Manual filter management
- Custom doFilterInternal implementation
- Complex OAuth2 integration

### **After (Spring Security 6)**
- Declarative SecurityFilterChain
- Native OAuth2 support
- Optimized filter chain
- Better caching

## 🎯 Best Practices

1. **Use SecurityFilterChain** thay vì custom filters
2. **Leverage OAuth2 Integration** cho Azure AD
3. **Implement Proper Error Handling** cho authentication failures
4. **Use Method-level Security** cho fine-grained access control
5. **Monitor Security Events** với proper logging

---

**Migration hoàn tất!** 🎉

Hệ thống hiện sử dụng Spring Security 6 với SecurityFilterChain và OAuth2 integration tốt hơn.
