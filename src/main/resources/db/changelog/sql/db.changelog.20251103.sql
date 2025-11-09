--liquibase formatted sql

-- changeset tuyen_hh:1760055000000

ALTER TABLE exam_session_students
    ADD COLUMN teacher_overall_feedback TEXT AFTER submitted_at;

ALTER TABLE exam_session_student_answers
    ADD COLUMN teacher_feedback TEXT AFTER answer_id;

-- changeset tuyen_hh:1760056000000
ALTER TABLE exam_session_students
    ADD CONSTRAINT uq_exam_session_student UNIQUE (exam_session_id, student_id);


-- changeset tuyen_hh:1760057000000
SET GLOBAL time_zone = '+07:00';

