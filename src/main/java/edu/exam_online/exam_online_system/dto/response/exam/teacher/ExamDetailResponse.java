package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.*;

import java.time.OffsetDateTime;
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
    private OffsetDateTime timeStart;
    private OffsetDateTime timeEnd;
    private List<QuestionResponse> questions;
}
