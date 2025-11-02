package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import edu.exam_online.exam_online_system.commons.constant.Difficulty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {
    private Long questionId;
    private String content;
    private Difficulty difficulty;
    private String explanation;
    private boolean shuffleAnswers;
    private boolean shuffleQuestions;
    private List<AnswerResponse> answers;
    private Float point;
    private Integer orderColumn;
}
