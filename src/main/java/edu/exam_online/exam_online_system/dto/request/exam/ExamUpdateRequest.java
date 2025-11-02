package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamUpdateRequest {
    @NotBlank(message = "Tên bài thi không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

}
