package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ExamCreationRequest {

    @NotBlank(message = "Tên kỳ thi không được để trống")
    @Size(max = 255, message = "Tên kỳ thi không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private String description;

    @Valid
    private List<QuestionCreationRequest> questions;


}
