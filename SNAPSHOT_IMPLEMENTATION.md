# Exam Session Snapshot Implementation Summary

## Problem
Khi exam được update sau khi tạo exam session, nội dung câu hỏi trong exam session cũng bị thay đổi theo. Điều này không hợp lý vì exam session đã được giao cho học sinh.

## Solution: Create Snapshot When Creating Exam Session

### 1. Database Changes ✅
- Created `exam_session_questions` table to store question snapshots
- Created `exam_session_answers` table to store answer snapshots
- Added foreign keys and indexes
- Migration file: `db.changelog.20251206-snapshot.sql`

### 2. Entity Classes ✅
- Created `ExamSessionQuestion.java` - snapshot of question content
- Created `ExamSessionAnswer.java` - snapshot of answer content
- Updated `ExamSession.java` to include relationship with snapshots

### 3. Repository Classes ✅
- Created `ExamSessionQuestionRepository.java`
- Created `ExamSessionAnswerRepository.java`

### 4. Service Layer Changes ✅
- Updated `ExamSessionServiceImpl.createSession()` to create snapshots
- Added `createExamSessionSnapshot()` method to copy questions and answers

### 5. Remaining Tasks  
- Update `ExamSessionStudentServiceImpl.newExamSessionStudent()` to use snapshot questions instead of original questions
- Update mapper to map from ExamSessionQuestion instead of Question
- Update DTO responses if needed
- Test the complete flow

## Key Benefits
1. **Data Integrity**: Exam sessions are immutable after creation
2. **Historical Accuracy**: Students see the same content that was assigned to them
3. **Audit Trail**: Original question IDs are preserved for reference
4. **Flexibility**: Teachers can modify exams without affecting active sessions

## Implementation Details
- Snapshots are created automatically when exam session is created
- Cascade delete ensures snapshots are removed when exam session is deleted
- Question and answer order is preserved
- All question attributes (content, difficulty, explanation, shuffle settings) are copied
