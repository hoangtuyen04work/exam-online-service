package edu.exam_online.exam_online_system.dto.response.exam.student;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class QuestionContentResponse {
    private Long questionId;
    private String content;
    private List<AnswerContentResponse> answers;
}
