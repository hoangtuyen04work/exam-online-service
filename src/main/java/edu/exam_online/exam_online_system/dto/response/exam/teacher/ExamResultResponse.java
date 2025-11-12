package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResultResponse {
    private Long id;
    private String studentName;
    private String examName;
    private double score;
    private OffsetDateTime submittedAt;
}
