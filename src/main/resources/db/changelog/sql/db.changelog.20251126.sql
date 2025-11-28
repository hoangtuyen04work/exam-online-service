--liquibase formatted sql

-- changeset tuyen_hh:1732588800000-1
-- Create classes table
CREATE TABLE classes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    semester VARCHAR(20),
    academic_year VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    teacher_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_classes_teacher FOREIGN KEY (teacher_id) REFERENCES user(id) ON DELETE CASCADE,
    INDEX idx_classes_teacher (teacher_id),
    INDEX idx_classes_class_code (class_code),
    INDEX idx_classes_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- changeset tuyen_hh:1732588800000-2
-- Create class_students table (many-to-many between classes and students)
CREATE TABLE class_students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_class_students_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
    CONSTRAINT fk_class_students_student FOREIGN KEY (student_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT uq_class_student UNIQUE (class_id, student_id),
    INDEX idx_class_students_class (class_id),
    INDEX idx_class_students_student (student_id),
    INDEX idx_class_students_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- changeset tuyen_hh:1732588800000-3
-- Create class_exam_sessions table (many-to-many between classes and exam sessions)
CREATE TABLE class_exam_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_id BIGINT NOT NULL,
    exam_session_id BIGINT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_class_exam_sessions_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
    CONSTRAINT fk_class_exam_sessions_exam_session FOREIGN KEY (exam_session_id) REFERENCES exam_sessions(id) ON DELETE CASCADE,
    CONSTRAINT uq_class_exam_session UNIQUE (class_id, exam_session_id),
    INDEX idx_class_exam_sessions_class (class_id),
    INDEX idx_class_exam_sessions_exam_session (exam_session_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
