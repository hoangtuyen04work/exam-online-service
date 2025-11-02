package edu.exam_online.exam_online_system.dto.response.exam.student;

import edu.exam_online.exam_online_system.commons.constant.ExamSessionStudentStateEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JoinExamSessionResponse {
    private Long examSessionId;
    private String name;
    private String description;
    private Integer durationMinutes;
    private ExamSessionStudentStateEnum state;
}
