package edu.exam_online.exam_online_system.dto.request.exam;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExitEventRequest {
    @NotNull(message = "Exam session student Id is required")
    private Long examSessionStudentId;
    private LocalDateTime eventTime; // optional
}
