--liquibase formatted sql

-- changeset tuyen_hh:1759835106000
CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


INSERT INTO role (name, description) VALUES
('ADMIN', 'Administrator role with full access'),
('TEACHER', 'Teacher role with exam management access'),
('STUDENT', 'Student role with exam taking access');


CREATE TABLE permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    resource VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO permission (name, description, resource, action) VALUES
-- Admin
('USER_MANAGE', 'Manage users', 'USER', 'MANAGE'),
('ROLE_MANAGE', 'Manage roles', 'ROLE', 'MANAGE'),
('EXAM_MANAGE', 'Manage all exams', 'EXAM', 'MANAGE'),

-- Teacher
('EXAM_CREATE', 'Create exams', 'EXAM', 'CREATE'),
('EXAM_UPDATE', 'Update own exams', 'EXAM', 'UPDATE'),
('EXAM_DELETE', 'Delete own exams', 'EXAM', 'DELETE'),
('EXAM_VIEW_RESULTS', 'View exam results', 'EXAM', 'VIEW_RESULTS'),

-- Student
('EXAM_TAKE', 'Take exams', 'EXAM', 'TAKE'),
('EXAM_VIEW_OWN_RESULTS', 'View own exam results', 'EXAM', 'VIEW_OWN_RESULTS');

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) ,
    last_name VARCHAR(50),
    phone VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO user (username, email, password, first_name, last_name, phone, is_active, is_email_verified)
VALUES (
    'admin',
    'admin@exam-online.com',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- password: admin123
    'System',
    'Administrator',
    '0123456789',
    TRUE,
    TRUE
);


CREATE TABLE user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
);

INSERT INTO user_role (user_id, role_id) VALUES (1, 1);



CREATE TABLE role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE,
    CONSTRAINT uk_role_permission UNIQUE (role_id, permission_id)
);

INSERT INTO role_permission (role_id, permission_id)
SELECT 1, id FROM permission;

INSERT INTO role_permission (role_id, permission_id) VALUES
(2, 4),
(2, 5), (2, 6), (2, 7);

INSERT INTO role_permission (role_id, permission_id) VALUES
(3, 8),
(3, 9);

CREATE TABLE token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(512) NOT NULL UNIQUE,
    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    is_revoked BOOLEAN NOT NULL DEFAULT FALSE,
    refresh_token_expires_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_token_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code_type ENUM('RESET_PASSWORD_CODE', 'FORGET_PASSWORD_CODE', 'REGISTER_CODE') NOT NULL,
    code VARCHAR(6) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_code_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);
