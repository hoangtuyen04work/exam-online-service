package edu.exam_online.exam_online_system.dto.response.exam.student.result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResultResponse {
    private Long questionId;
    private String content;
    private String explanation;
    private String teacherFeedback;
    private List<AnswerResultResponse> answers;
}
