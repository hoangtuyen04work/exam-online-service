package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamUpdateQuestionsRequest {

    @NotBlank(message = "Tên bài thi không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Tổng điểm không được để trống")
    @Positive(message = "Tổng điểm phải lớn hơn 0")
    private Integer totalPoint;

    @NotNull(message = "Thời lượng làm bài không được để trống")
    @Positive(message = "Thời lượng phải lớn hơn 0")
    private Integer durationMinutes;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @Future(message = "Thời gian bắt đầu phải ở tương lai")
    private LocalDateTime timeStart;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    @Future(message = "Thời gian kết thúc phải ở tương lai")
    private LocalDateTime timeEnd;

    @NotNull(message = "Danh sách câu hỏi không được để trống")
    @Size(min = 1, message = "Phải có ít nhất một câu hỏi trong đề thi")
    private List<@Valid QuestionUpdateRequest> questions;
}

