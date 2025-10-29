package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionExamRequest {

    @NotNull(message = "Exam ID không được để trống")
    private Long examId;

    @NotNull(message = "Question ID không được để trống")
    private Long questionId;

    @NotNull(message = "Điểm của câu hỏi trong kỳ thi không được để trống")
    @DecimalMin(value = "0.1", message = "Điểm phải lớn hơn 0")
    private Float point;

    private Integer orderColumn;
}
