package edu.exam_online.exam_online_system.dto.response.exam.student;

import edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSessionStudentResponse {
    private Long examSessionId;
    private String examSessionName;
    private Float totalScore;       // có thể null nếu chưa có điểm
    private ExamStudentStatusEnum status;
    private LocalDateTime submittedAt;
}