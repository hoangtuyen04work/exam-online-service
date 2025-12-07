package edu.exam_online.exam_online_system.dto.request.exam;

import edu.exam_online.exam_online_system.commons.constant.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExamQuestionLevelRequest {

    @NotNull(message = "Quantity is required")
    private Long quantity;

    @NotBlank(message = "Difficulty is required")
    private Difficulty difficulty;
}
