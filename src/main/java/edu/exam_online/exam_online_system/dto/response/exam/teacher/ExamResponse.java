package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponse {
    private Long examId;
    private String name;
    private String description;
    private Integer numberQuestions;
}
