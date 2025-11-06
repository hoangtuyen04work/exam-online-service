--liquibase formatted sql

-- changeset tuyen_hh:1760055000000

ALTER TABLE exam_session_students
    ADD COLUMN teacher_overall_feedback TEXT AFTER submitted_at;

ALTER TABLE exam_session_student_answers
    ADD COLUMN teacher_feedback TEXT AFTER answer_id;