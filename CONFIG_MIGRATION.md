# Configuration Migration: YAML to Properties

## ✅ Đã chuyển đổi thành công!

Cấu hình đã được chuyển từ `application.yaml` sang `application.properties`.

## 📁 File Changes

### ❌ Removed:
- `src/main/resources/application.yaml`

### ✅ Added:
- `src/main/resources/application.properties`

## 🔄 Mapping Configuration

### Server Configuration
```yaml
# YAML
server:
  port: 8888
  servlet:
    context-path: /exam-online-system
```
```properties
# Properties
server.port=8888
server.servlet.context-path=/exam-online-system
```

### Database Configuration
```yaml
# YAML
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/exam_online_system?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
```
```properties
# Properties
spring.datasource.url=jdbc:mysql://localhost:3306/exam_online_system?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### JPA Configuration
```yaml
# YAML
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
```
```properties
# Properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true
```

### Azure AD Configuration
```yaml
# YAML
spring:
  cloud:
    azure:
      active-directory:
        enabled: true
        tenant-id: ${AZURE_TENANT_ID:your-tenant-id}
        client-id: ${AZURE_CLIENT_ID:your-client-id}
        client-secret: ${AZURE_CLIENT_SECRET:your-client-secret}
        redirect-uri-template: http://localhost:8888/exam-online-system/login/oauth2/code/azure
        scope: openid,profile,email,User.Read
        graph-membership-uri: https://graph.microsoft.com/v1.0/me/memberOf
```
```properties
# Properties
spring.cloud.azure.active-directory.enabled=true
spring.cloud.azure.active-directory.tenant-id=${AZURE_TENANT_ID:your-tenant-id}
spring.cloud.azure.active-directory.client-id=${AZURE_CLIENT_ID:your-client-id}
spring.cloud.azure.active-directory.client-secret=${AZURE_CLIENT_SECRET:your-client-secret}
spring.cloud.azure.active-directory.redirect-uri-template=http://localhost:8888/exam-online-system/login/oauth2/code/azure
spring.cloud.azure.active-directory.scope=openid,profile,email,User.Read
spring.cloud.azure.active-directory.graph-membership-uri=https://graph.microsoft.com/v1.0/me/memberOf
```

### OAuth2 Configuration
```yaml
# YAML
spring:
  security:
    oauth2:
      client:
        registration:
          azure:
            client-id: ${AZURE_CLIENT_ID:your-client-id}
            client-secret: ${AZURE_CLIENT_SECRET:your-client-secret}
            scope: openid,profile,email,User.Read
            redirect-uri: http://localhost:8888/exam-online-system/login/oauth2/code/azure
            authorization-grant-type: authorization_code
        provider:
          azure:
            authorization-uri: https://login.microsoftonline.com/${AZURE_TENANT_ID:your-tenant-id}/oauth2/v2.0/authorize
            token-uri: https://login.microsoftonline.com/${AZURE_TENANT_ID:your-tenant-id}/oauth2/v2.0/token
            user-info-uri: https://graph.microsoft.com/v1.0/me
            user-name-attribute: id
```
```properties
# Properties
spring.security.oauth2.client.registration.azure.client-id=${AZURE_CLIENT_ID:your-client-id}
spring.security.oauth2.client.registration.azure.client-secret=${AZURE_CLIENT_SECRET:your-client-secret}
spring.security.oauth2.client.registration.azure.scope=openid,profile,email,User.Read
spring.security.oauth2.client.registration.azure.redirect-uri=http://localhost:8888/exam-online-system/login/oauth2/code/azure
spring.security.oauth2.client.registration.azure.authorization-grant-type=authorization_code

spring.security.oauth2.client.provider.azure.authorization-uri=https://login.microsoftonline.com/${AZURE_TENANT_ID:your-tenant-id}/oauth2/v2.0/authorize
spring.security.oauth2.client.provider.azure.token-uri=https://login.microsoftonline.com/${AZURE_TENANT_ID:your-tenant-id}/oauth2/v2.0/token
spring.security.oauth2.client.provider.azure.user-info-uri=https://graph.microsoft.com/v1.0/me
spring.security.oauth2.client.provider.azure.user-name-attribute=id
```

### Logging Configuration
```yaml
# YAML
logging:
  level:
    com.azure.spring: DEBUG
    org.springframework.security: DEBUG
```
```properties
# Properties
logging.level.com.azure.spring=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.edu.exam_online.exam_online_system=DEBUG
```

## 🆕 Additional Properties

Đã thêm một số cấu hình mới trong properties:

```properties
# JWT Configuration
jwt.secret=${JWT_SECRET:mySecretKey}
jwt.expiration=${JWT_EXPIRATION:86400000}

# Additional Logging
logging.level.edu.exam_online.exam_online_system=DEBUG
```

## ✅ Lợi ích của Properties Format

1. **Đơn giản hơn** - Không cần indentation
2. **Dễ đọc** - Một dòng một property
3. **IDE Support** - Autocomplete tốt hơn
4. **Environment Variables** - Dễ dàng override
5. **Legacy Support** - Tương thích với các hệ thống cũ

## 🚀 Sử dụng

Cấu hình mới hoạt động giống hệt như trước, chỉ khác format:

```bash
# Chạy ứng dụng
./gradlew bootRun

# Hoặc với environment variables
export AZURE_TENANT_ID=your-tenant-id
export AZURE_CLIENT_ID=your-client-id
export AZURE_CLIENT_SECRET=your-client-secret
./gradlew bootRun
```

## 📝 Lưu ý

- Tất cả functionality vẫn hoạt động bình thường
- Environment variables vẫn được hỗ trợ
- Không cần thay đổi code Java
- Chỉ thay đổi format configuration file

---

**Migration hoàn tất!** 🎉
