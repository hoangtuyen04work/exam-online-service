# Azure AD Authentication Setup Guide

## 🚀 Tổng quan

Hệ thống đã được cấu hình để hỗ trợ authentication với Azure AD (Azure Active Directory) cho tài khoản edu. Người dùng có thể đăng nhập bằng tài khoản Azure AD của trường học.

## 📋 Yêu cầu

1. **Azure AD Tenant** - Tài khoản Azure AD của trường học
2. **App Registration** - Ứng dụng đã được đăng ký trong Azure AD
3. **MySQL Database** - Database để lưu trữ thông tin người dùng

## 🔧 Cấu hình Azure AD

### Bước 1: Tạo App Registration trong Azure Portal

1. Đăng nhập vào [Azure Portal](https://portal.azure.com)
2. Vào **Azure Active Directory** > **App registrations**
3. Click **New registration**
4. Điền thông tin:
   - **Name**: `Exam Online System`
   - **Supported account types**: `Accounts in this organizational directory only`
   - **Redirect URI**: `Web` - `http://localhost:8888/exam-online-system/login/oauth2/code/azure`
5. Click **Register**

### Bước 2: Cấu hình Authentication

1. Vào **Authentication** trong app registration
2. Thêm **Redirect URI**:
   - `http://localhost:8888/exam-online-system/login/oauth2/code/azure`
3. Enable **ID tokens** và **Access tokens**
4. Click **Save**

### Bước 3: Tạo Client Secret

1. Vào **Certificates & secrets**
2. Click **New client secret**
3. Điền **Description**: `Exam Online System Secret`
4. Chọn **Expires**: `24 months`
5. Click **Add**
6. **Copy và lưu lại** Client Secret (chỉ hiển thị 1 lần)

### Bước 4: Cấu hình API Permissions

1. Vào **API permissions**
2. Click **Add a permission**
3. Chọn **Microsoft Graph**
4. Chọn **Delegated permissions**
5. Thêm các permissions:
   - `User.Read`
   - `User.ReadBasic.All`
   - `openid`
   - `profile`
   - `email`
6. Click **Grant admin consent**

## ⚙️ Cấu hình Environment Variables

Tạo file `.env` hoặc cấu hình environment variables:

```bash
# Azure AD Configuration
AZURE_TENANT_ID=your-tenant-id-here
AZURE_CLIENT_ID=your-client-id-here
AZURE_CLIENT_SECRET=your-client-secret-here

# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/exam_online_system
DB_USERNAME=root
DB_PASSWORD=your-database-password

# JWT Configuration
JWT_SECRET=your-jwt-secret-key-here
JWT_EXPIRATION=86400000
```

## 🗄️ Database Setup

### Tạo Database

```sql
CREATE DATABASE exam_online_system;
```

### Tạo Tables (sẽ được tạo tự động bởi JPA)

Hệ thống sẽ tự động tạo các bảng:
- `user` - Thông tin người dùng
- `role` - Vai trò người dùng
- `permission` - Quyền hạn
- `user_role` - Liên kết user-role
- `role_permission` - Liên kết role-permission

## 🚀 Chạy ứng dụng

1. **Cấu hình environment variables**:
   ```bash
   export AZURE_TENANT_ID=your-tenant-id
   export AZURE_CLIENT_ID=your-client-id
   export AZURE_CLIENT_SECRET=your-client-secret
   ```

2. **Chạy ứng dụng**:
   ```bash
   ./gradlew bootRun
   ```

3. **Truy cập ứng dụng**:
   - URL: `http://localhost:8888/exam-online-system`
   - Azure Login: `http://localhost:8888/exam-online-system/oauth2/authorization/azure`

## 📡 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Đăng nhập với username/password |
| POST | `/api/auth/register` | Đăng ký tài khoản mới |
| GET | `/api/auth/me` | Lấy thông tin user hiện tại |
| POST | `/api/auth/logout` | Đăng xuất |
| GET | `/api/auth/check-email` | Kiểm tra email có sẵn |
| GET | `/api/auth/check-username` | Kiểm tra username có sẵn |

### Azure AD Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/auth/azure/login` | Redirect đến Azure login |
| GET | `/api/auth/azure/callback` | Callback từ Azure AD |
| GET | `/api/auth/azure/user-info` | Thông tin user từ Azure |
| POST | `/api/auth/azure/logout` | Đăng xuất Azure |

### Test Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/test/public` | Endpoint công khai |
| GET | `/api/test/protected` | Endpoint yêu cầu authentication |
| GET | `/api/test/user-info` | Thông tin user hiện tại |

## 🔐 Security Features

1. **JWT Authentication** - Token-based authentication
2. **Azure AD Integration** - Single Sign-On với tài khoản edu
3. **Role-based Access Control** - Phân quyền theo vai trò
4. **CORS Configuration** - Hỗ trợ cross-origin requests
5. **Password Encryption** - Mã hóa mật khẩu với BCrypt

## 👥 User Roles

### ADMIN
- Quản lý tất cả users
- Quản lý roles và permissions
- Quản lý tất cả exams

### TEACHER
- Tạo và quản lý exams
- Xem kết quả exams
- Quản lý students

### STUDENT
- Tham gia exams
- Xem kết quả của mình

## 🧪 Testing

### Test với Postman/curl

1. **Đăng ký tài khoản**:
   ```bash
   curl -X POST http://localhost:8888/exam-online-system/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "username": "testuser",
       "email": "test@example.com",
       "password": "password123",
       "confirmPassword": "password123",
       "firstName": "Test",
       "lastName": "User"
     }'
   ```

2. **Đăng nhập**:
   ```bash
   curl -X POST http://localhost:8888/exam-online-system/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "usernameOrEmail": "test@example.com",
       "password": "password123"
     }'
   ```

3. **Test protected endpoint**:
   ```bash
   curl -X GET http://localhost:8888/exam-online-system/api/test/protected \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

## 🔧 Troubleshooting

### Lỗi thường gặp

1. **Azure AD Configuration Error**:
   - Kiểm tra Tenant ID, Client ID, Client Secret
   - Đảm bảo Redirect URI đúng
   - Kiểm tra API permissions

2. **Database Connection Error**:
   - Kiểm tra MySQL service đang chạy
   - Kiểm tra database credentials
   - Đảm bảo database tồn tại

3. **JWT Token Error**:
   - Kiểm tra JWT secret key
   - Kiểm tra token expiration
   - Đảm bảo token được gửi đúng format

### Logs

Kiểm tra logs để debug:
```bash
tail -f logs/application.log
```

## 📞 Support

Nếu gặp vấn đề, hãy kiểm tra:
1. Azure AD configuration
2. Database connection
3. Environment variables
4. Application logs

---

**Lưu ý**: Đảm bảo thay thế các giá trị placeholder bằng thông tin thực tế từ Azure AD của bạn.
