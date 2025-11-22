package edu.exam_online.exam_online_system.dto.response.exam.student;

import edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ExamSessionContentResponse {
    @Builder.Default
    private ExamStudentStatusEnum status = ExamStudentStatusEnum.IN_PROGRESS;
    private Long examSessionId;
    private Long examSessionStudentId;
    private String name;
    private OffsetDateTime  startedAt;
    private OffsetDateTime expiredAt;
    private Integer durationMinutes;
    private List<QuestionContentResponse> questions;
}
