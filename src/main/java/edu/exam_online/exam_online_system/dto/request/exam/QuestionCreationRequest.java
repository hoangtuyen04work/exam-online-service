package edu.exam_online.exam_online_system.dto.request.exam;

import edu.exam_online.exam_online_system.commons.constant.Difficulty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionCreationRequest {

    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    private String content;

    @NotNull(message = "Điểm của câu hỏi không được để trống")
    @DecimalMin(value = "0.1", message = "Điểm phải lớn hơn 0")
    private Float point;

    @Min(value = 0)
    private Integer orderColumn;

    private boolean shuffleAnswers;

    private boolean shuffleQuestions;

    @NotNull(message = "Độ khó không được để trống")
    private Difficulty difficulty;

    @Size(max = 2000, message = "Giải thích không được vượt quá 2000 ký tự")
    private String explanation;

    @Valid
    private List<AnswerCreationRequest> answers;
}
