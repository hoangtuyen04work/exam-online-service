-- liquibase formatted sql

-- changeset tuyen_hh:1732762000000-1
-- Add allow_student_chat column to classes table
ALTER TABLE exam_sessions
ADD COLUMN passing_score DOUBLE;


-- changeset tuyen_hh:1732772000000-1

-- Create exam_session_question_snapshot table (snapshot of questions when exam session is created)
CREATE TABLE exam_session_question_snapshot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_session_id BIGINT NOT NULL,
    original_question_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    shuffle_answers BOOLEAN DEFAULT FALSE,
    shuffle_questions BOOLEAN DEFAULT FALSE,
    difficulty VARCHAR(50),
    explanation TEXT,
    question_order INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question_snapshot_session 
        FOREIGN KEY (exam_session_id) REFERENCES exam_sessions(id) ON DELETE CASCADE,
    INDEX idx_question_snapshot_session (exam_session_id),
    INDEX idx_question_snapshot_original (original_question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT = 'Snapshot of question content when exam session is created to prevent changes from affecting active sessions';

-- Create exam_session_answer_snapshot table (snapshot of answers when exam session is created)
CREATE TABLE exam_session_answer_snapshot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_session_question_snapshot_id BIGINT NOT NULL,
    original_answer_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    answer_order INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_answer_snapshot_question 
        FOREIGN KEY (exam_session_question_snapshot_id) REFERENCES exam_session_question_snapshot(id) ON DELETE CASCADE,
    INDEX idx_answer_snapshot_question (exam_session_question_snapshot_id),
    INDEX idx_answer_snapshot_original (original_answer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT = 'Snapshot of answer content when exam session is created to prevent changes from affecting active sessions';



-- liquibase formatted sql

-- changeset tuyen_hh:1733500000000-1
-- Update exam_session_student_answers to reference snapshot tables

-- Modify exam_session_student_answers table to use snapshot references
ALTER TABLE exam_session_student_answers
    ADD COLUMN exam_session_question_snapshot_id BIGINT NOT NULL,
    ADD COLUMN exam_session_answer_snapshot_id BIGINT;
-- changeset tuyen_hh:1733500100000-1
-- Add foreign keys to snapshot tables
ALTER TABLE exam_session_student_answers
    ADD CONSTRAINT fk_student_answer_question_snapshot
        FOREIGN KEY (exam_session_question_snapshot_id) REFERENCES exam_session_question_snapshot(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_student_answer_answer_snapshot
        FOREIGN KEY (exam_session_answer_snapshot_id) REFERENCES exam_session_answer_snapshot(id) ON DELETE SET NULL;

-- Add indexes for performance
CREATE INDEX idx_student_answer_question_snapshot ON exam_session_student_answers(exam_session_question_snapshot_id);
CREATE INDEX idx_student_answer_answer_snapshot ON exam_session_student_answers(exam_session_answer_snapshot_id);

-- Add comment to explain the table purpose
ALTER TABLE exam_session_student_answers COMMENT = 'Student answers referencing snapshot questions and answers to maintain consistency';

