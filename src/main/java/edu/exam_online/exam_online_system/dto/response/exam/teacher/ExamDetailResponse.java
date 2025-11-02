package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamDetailResponse {
    private Long examId;
    private String name;
    private String description;
    private Integer totalPoint;
    private int durationMinutes;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private List<QuestionResponse> questions;
}
