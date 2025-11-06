package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentJoinedExamSessionResponse {
    private Long examSessionStudentId;
    private String studentName;
    private Long studentId;
    private ExamStudentStatusEnum status;
    private LocalDateTime submittedAt;
    private float score;
}
