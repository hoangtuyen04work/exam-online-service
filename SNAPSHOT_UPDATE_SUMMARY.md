# Tóm tắt thay đổi: Snapshot System cho Exam Session

## Ngày: 2024-12-06

## Mục tiêu
Cập nhật hệ thống để đảm bảo rằng khi giáo viên giao ExamSession, nội dung bài thi sẽ được "đóng băng" (snapshot). Việc sửa đổi Exam gốc sau đó sẽ không ảnh hưởng đến ExamSession đã được giao cho học sinh.

## Thay đổi chi tiết

### 1. Entities

#### 1.1. ExamSessionStudentAnswer
**File:** `entity/exam/ExamSessionStudentAnswer.java`
- ✅ Đã tồn tại: ExamSessionQuestion và ExamSessionAnswer (các bảng snapshot)
- ✅ **Thay đổi:** Cập nhật references từ `Question` và `Answer` gốc sang `ExamSessionQuestion` và `ExamSessionAnswer` snapshot
  ```java
  // Trước
  @ManyToOne
  private Question question;
  @ManyToOne
  private Answer selectedAnswer;
  
  // Sau
  @ManyToOne
  private ExamSessionQuestion examSessionQuestion;
  @ManyToOne
  private ExamSessionAnswer selectedAnswer;
  ```

#### 1.2. ExamSessionQuestion
**File:** `entity/exam/ExamSessionQuestion.java`
- ✅ Đã tồn tại snapshot của Question
- ✅ **Thêm:** Relationship với ExamSessionStudentAnswer
  ```java
  @OneToMany(mappedBy = "examSessionQuestion")
  private List<ExamSessionStudentAnswer> studentAnswers = new ArrayList<>();
  ```

#### 1.3. ExamSessionAnswer
**File:** `entity/exam/ExamSessionAnswer.java`
- ✅ Đã tồn tại snapshot của Answer
- ✅ **Thêm:** Relationship với ExamSessionStudentAnswer
  ```java
  @OneToMany(mappedBy = "selectedAnswer")
  private List<ExamSessionStudentAnswer> studentAnswers = new ArrayList<>();
  ```

#### 1.4. Question và Answer
**File:** `entity/exam/Question.java` và `entity/exam/Answer.java`
- ✅ **Xóa:** Relationship với ExamSessionStudentAnswer (không còn cần thiết)

### 2. Mapper

#### 2.1. ExamSessionStudentAnswerMapper
**File:** `mapper/ExamSessionStudentAnswerMapper.java`
- ✅ **Cập nhật:** Chỉ giữ lại method `toEntityFromSnapshot()` để tạo student answer từ snapshot question
- ✅ **Xóa:** Method cũ `toEntity()` sử dụng Question gốc

#### 2.2. ExamSessionStudentMapper
**File:** `mapper/ExamSessionStudentMapper.java`
- ✅ **Cập nhật toàn bộ methods:** Sử dụng `examSessionQuestion` thay vì `question`
  - `updateEntity()`: Dùng `getOriginalQuestionId()`
  - `mapQuestionResult()`: Dùng snapshot content
  - `mapAnswers()`: Dùng `getExamSessionAnswers()`
  - `toAnswerResultResponse()`: Dùng `getOriginalAnswerId()` và `getIsCorrect()`
  - `toQuestionContentResponse()`: Dùng snapshot data
  - `toAnswerContentResponse()`: Dùng snapshot answers

### 3. Services

#### 3.1. ExamSessionServiceImpl
**File:** `service/exam/impl/ExamSessionServiceImpl.java`
- ✅ **Đã tồn tại:** Method `createExamSessionSnapshot()` tạo snapshot khi tạo ExamSession
- ✅ **Xóa:** Unused imports

#### 3.2. ExamSessionStudentServiceImpl
**File:** `service/exam/impl/ExamSessionStudentServiceImpl.java`
- ✅ **Cập nhật:** `newExamSessionStudent()` - Chỉ sử dụng snapshot, xóa fallback logic
- ✅ **Cập nhật:** `saveDraftExamSessionStudentAnswer()` - Map student answers đến snapshot answers
- ✅ **Cập nhật:** `saveFinalExamSessionStudentAnswer()` - Map student answers đến snapshot answers
- ✅ **Cập nhật:** `computeScore()` - Sử dụng `getIsCorrect()` từ snapshot answer
- ✅ **Xóa:** Imports không dùng đến (Answer, Question, AnswerRepository)
- ✅ **Fix:** Các tham số không sử dụng

### 4. Database Migration

#### 4.1. Bảng Snapshot (Đã tồn tại)
**File:** `db/changelog/sql/db.changelog.20251206.sql`
- ✅ `exam_session_questions` - Snapshot của questions
- ✅ `exam_session_answers` - Snapshot của answers

#### 4.2. Update ExamSessionStudentAnswer
**File:** `db/changelog/sql/db.changelog.20251206-2.sql` (MỚI)
- ✅ Thêm columns: `exam_session_question_id`, `exam_session_answer_id`
- ✅ Thêm foreign keys đến snapshot tables
- ✅ Xóa old columns: `question_id`, `answer_id`
- ✅ Thêm indexes cho performance

### 5. Exception Handling

**File:** `exception/ErrorCode.java`
- ✅ **Thêm:** `EXAM_SESSION_SNAPSHOT_NOT_FOUND` - Error khi snapshot không tồn tại

## Luồng hoạt động mới

### 1. Khi giáo viên tạo ExamSession
```
1. Giáo viên tạo ExamSession từ Exam
2. ExamSessionServiceImpl.createSession()
   └─> createExamSessionSnapshot()
       ├─> Copy mỗi Question → ExamSessionQuestion
       └─> Copy mỗi Answer → ExamSessionAnswer
3. Snapshot được lưu vào DB
```

### 2. Khi học sinh tham gia làm bài
```
1. Student join ExamSession
2. ExamSessionStudentServiceImpl.newExamSessionStudent()
   ├─> Load ExamSessionQuestions (snapshot)
   └─> Create ExamSessionStudentAnswers referencing snapshots
3. Student answers được lưu với reference đến snapshot
```

### 3. Khi học sinh lưu câu trả lời
```
1. Student chọn answer (gửi originalAnswerId)
2. Service map originalAnswerId → ExamSessionAnswer (snapshot)
3. Save ExamSessionStudentAnswer với reference đến snapshot answer
```

### 4. Khi tính điểm và hiển thị kết quả
```
1. Lấy ExamSessionStudentAnswers
2. Check ExamSessionAnswer.isCorrect từ snapshot
3. Hiển thị content từ snapshot (không bị ảnh hưởng bởi changes trên Exam gốc)
```

## Lợi ích

1. ✅ **Tính nhất quán:** ExamSession giữ nguyên nội dung tại thời điểm tạo
2. ✅ **Cách ly dữ liệu:** Sửa Exam gốc không ảnh hưởng ExamSession đã giao
3. ✅ **Audit trail:** Có thể xem lại chính xác nội dung bài thi lúc học sinh làm
4. ✅ **Performance:** Không cần join nhiều bảng để lấy historical data

## Test Cases cần kiểm tra

1. ✅ Tạo ExamSession → Verify snapshot được tạo
2. ✅ Sửa Question/Answer trong Exam gốc → Verify ExamSession không thay đổi
3. ✅ Student làm bài → Verify sử dụng snapshot data
4. ✅ Tính điểm → Verify sử dụng snapshot isCorrect
5. ✅ Xem kết quả → Verify hiển thị snapshot content

## Files đã thay đổi

### Entity
- `entity/exam/ExamSessionStudentAnswer.java` - Updated references
- `entity/exam/ExamSessionQuestion.java` - Added relationship
- `entity/exam/ExamSessionAnswer.java` - Added relationship  
- `entity/exam/Question.java` - Removed relationship
- `entity/exam/Answer.java` - Removed relationship

### Mapper
- `mapper/ExamSessionStudentAnswerMapper.java` - Simplified to use snapshots only
- `mapper/ExamSessionStudentMapper.java` - Updated all methods

### Service
- `service/exam/impl/ExamSessionServiceImpl.java` - Cleaned imports
- `service/exam/impl/ExamSessionStudentServiceImpl.java` - Major refactoring

### Repository
- (No changes - repositories already exist)

### Database
- `db/changelog/sql/db.changelog.20251206-2.sql` - NEW migration file

### Exception
- `exception/ErrorCode.java` - Added new error code

## Migration Notes

⚠️ **QUAN TRỌNG:** Trước khi chạy migration:
1. Backup database
2. Đảm bảo không có ExamSession đang active
3. Chạy migration trong maintenance window
4. Verify data integrity sau migration

## Rollback Plan

Nếu cần rollback:
1. Restore database từ backup
2. Revert code changes
3. Restart application

## Completed Status

✅ All entities updated
✅ All mappers updated  
✅ All services updated
✅ Migration script created
✅ Error handling added
✅ Code compiled successfully
✅ Documentation created
