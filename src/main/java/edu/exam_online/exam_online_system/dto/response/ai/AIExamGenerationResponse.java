package edu.exam_online.exam_online_system.dto.response.ai;

import edu.exam_online.exam_online_system.dto.response.exam.teacher.QuestionResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIExamGenerationResponse {
    private Long examId;
    private String examName;
    private String examDescription;
    private Integer totalQuestions;
    private Float totalPoints;
    private String aiSuggestion;
    private List<QuestionResponse> questions;
}
