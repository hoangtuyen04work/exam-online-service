--liquibase formatted sql

-- changeset tuyen_hh:1759935106000
CREATE TABLE bank_questions
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    teacher_id BIGINT NOT NULL,
    description TEXT,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_bank_question_teacher FOREIGN KEY (teacher_id) REFERENCES user(id),
    CONSTRAINT fk_bank_question_created_by FOREIGN KEY (created_by) REFERENCES user(id)
);

CREATE TABLE exams
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    teacher_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_exam_teacher FOREIGN KEY (teacher_id) REFERENCES user(id),
    CONSTRAINT fk_exam_created_by FOREIGN KEY (created_by) REFERENCES user(id)
);

CREATE TABLE questions
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    shuffle_answers BOOLEAN DEFAULT FALSE,
    shuffle_questions BOOLEAN DEFAULT FALSE,
    difficulty ENUM('EASY', 'MEDIUM', 'HARD'),
    explanation TEXT,
    bank_question_id BIGINT,
    created_by BIGINT NOT NULL,
    CONSTRAINT fk_question_bank_question FOREIGN KEY (bank_question_id) REFERENCES bank_questions(id),
    CONSTRAINT fk_question_created_by FOREIGN KEY (created_by) REFERENCES user(id)
);

CREATE TABLE answers
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    question_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES questions(id),
    CONSTRAINT fk_answer_created_by FOREIGN KEY (created_by) REFERENCES user(id)
);

CREATE TABLE question_exam
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    point FLOAT NOT NULL,
    order_column INT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_question_exam_exam FOREIGN KEY (exam_id) REFERENCES exams(id),
    CONSTRAINT fk_question_exam_question FOREIGN KEY (question_id) REFERENCES questions(id)
);
