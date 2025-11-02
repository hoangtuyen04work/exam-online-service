package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveQuestionRequest {
    @NotNull(message = "Exam Id is required")
    private Long questionId;

    @NotNull(message = "Answer Id is required")
    private Long answerId;
}
