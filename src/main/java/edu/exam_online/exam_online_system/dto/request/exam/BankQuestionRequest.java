package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BankQuestionRequest {

    @NotBlank(message = "Tên ngân hàng câu hỏi không được để trống")
    @Size(max = 255, message = "Tên ngân hàng câu hỏi không được vượt quá 255 ký tự")
    private String name;

    @NotNull(message = "Teacher ID không được để trống")
    private Long teacherId;

    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private String description;

    @Valid
    private List<QuestionCreationRequest> questions;
}
