package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankQuestionResponse {
    private Long id;
    private String name;
    private String description;
    private String teacherName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
