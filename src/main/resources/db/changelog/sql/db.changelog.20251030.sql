--liquibase formatted sql

-- changeset tuyen_hh:1760045000000
CREATE TABLE exam_sessions
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    exam_id BIGINT NOT NULL,
    owner_id BIGINT NOT NULL,
    start_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expired_at DATETIME NULL,
    created_by BIGINT NOT NULL,
    duration_minutes INT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_exam_sessions_user FOREIGN KEY (owner_id) REFERENCES user (id) ON DELETE CASCADE,
    CONSTRAINT fk_exam_sessions_exam FOREIGN KEY (exam_id) REFERENCES exams (id) ON DELETE CASCADE
);

CREATE TABLE exam_session_students
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    exam_session_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    status ENUM('IN_PROGRESS', 'COMPLETED') DEFAULT 'IN_PROGRESS' NOT NULL,
    started_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submitted_at DATETIME NULL,
    expired_at DATETIME NULL,
    exit_count INT DEFAULT 0,
    total_score FLOAT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_exam_students_exam FOREIGN KEY (exam_session_id) REFERENCES exam_sessions (id) ON DELETE CASCADE,
    CONSTRAINT fk_exam_students_student FOREIGN KEY (student_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE exam_session_student_answers
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    exam_session_student_id BIGINT NOT NULL,
    state ENUM('NOT_OPEN', 'OPENING', 'CLOSED') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ess_answers_session_student FOREIGN KEY (exam_session_student_id) REFERENCES exam_session_students (id) ON DELETE CASCADE
);

CREATE INDEX idx_exam_sessions_time ON exam_sessions (start_at, expired_at);
CREATE INDEX idx_exam_session_students_status ON exam_session_students (exam_session_id, status);
CREATE INDEX idx_ess_answers_session ON exam_session_student_answers (exam_session_student_id);
