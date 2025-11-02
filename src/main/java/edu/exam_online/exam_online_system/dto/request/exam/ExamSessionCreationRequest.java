package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamSessionCreationRequest {

    @NotNull(message = "Exam id is required")
    private Long examId;

    @NotBlank(message = "Session name is required")
    private String name;

    @NotNull(message = "Session duration is required")
    @Min(value = 1, message = "Session duration must be greater than 0")
    private Integer durationMinutes;

    private String description;

    private LocalDateTime expiredAt;

    private LocalDateTime createdAt;
}
