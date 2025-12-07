-- liquibase formatted sql

-- changeset tuyen_hh:1732762000000-1
-- Add allow_student_chat column to classes table
ALTER TABLE classes
ADD COLUMN allow_student_chat BOOLEAN NOT NULL DEFAULT TRUE;

-- Create class_messages table
CREATE TABLE class_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_class_messages_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
    CONSTRAINT fk_class_messages_sender FOREIGN KEY (sender_id) REFERENCES user(id) ON DELETE CASCADE,
    INDEX idx_class_messages_class_id (class_id),
    INDEX idx_class_messages_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
