package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ExamCreationRequest {

    @NotBlank(message = "Tên kỳ thi không được để trống")
    @Size(max = 255, message = "Tên kỳ thi không được vượt quá 255 ký tự")
    private String name;

    @NotNull(message = "Thời lượng không được để trống")
    @Min(value = 1, message = "Thời lượng phải lớn hơn hoặc bằng 1 phút")
    private Integer durationMinutes;

    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private String description;

    @NotNull(message = "Tổng điểm không được để trống")
    @Min(value = 1, message = "Tổng điểm phải lớn hơn hoặc bằng 1")
    private Integer totalPoint;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalDateTime timeStart;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    private LocalDateTime timeEnd;

    @Valid
    private List<QuestionCreationRequest> questions;


}
