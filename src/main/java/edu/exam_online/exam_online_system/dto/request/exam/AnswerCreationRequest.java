package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerCreationRequest {

    @NotBlank(message = "Nội dung đáp án không được để trống")
    private String content;

    @NotNull(message = "Trường isCorrect không được để trống")
    private Boolean correct;

}
