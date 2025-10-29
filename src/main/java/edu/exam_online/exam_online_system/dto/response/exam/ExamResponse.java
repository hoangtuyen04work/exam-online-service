package edu.exam_online.exam_online_system.dto.response.exam;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponse {
    private Long examId;
    private String name;
    private String description;
    private String totalPoint;
    private Integer numberQuestions;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
}
