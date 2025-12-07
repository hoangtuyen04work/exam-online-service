package edu.exam_online.exam_online_system.dto.response.exam.student.result;

import edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSessionStudentResultResponse {
    private Long examSessionId;
    private String examSessionName;
    private Float totalScore;
    private ExamStudentStatusEnum status;
    private OffsetDateTime submittedAt;
    private Integer exitCount;
    private String teacherOverallFeedback;
    private List<QuestionResultResponse> questions;
    private Double passingScore;
    private Boolean isPassed; // true if totalScore >= passingScore
}