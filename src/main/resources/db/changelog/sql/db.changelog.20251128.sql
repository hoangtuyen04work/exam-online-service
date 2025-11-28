--liquibase formatted sql

-- changeset tuyen_hh:1732752000000-1
-- Drop unique constraint on class_exam_sessions to allow multiple assignments of same exam session to a class
-- This enables a class to take the same exam session multiple times
ALTER TABLE class_exam_sessions DROP CONSTRAINT uq_class_exam_session;

-- changeset tuyen_hh:1732752000000-2
-- Add comment to clarify the change
-- Note: Same exam session can now be assigned multiple times to the same class
-- Each assignment will have a different 'assigned_at' timestamp
