package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExamImportRequest {

    @NotBlank(message = "Tên kỳ thi không được để trống")
    @Size(max = 255, message = "Tên kỳ thi không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private String description;

}
