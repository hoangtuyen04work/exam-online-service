package edu.exam_online.exam_online_system.dto.response.exam.student;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerContentResponse {
    private String content;
    private Long answerId;
    private boolean isSelected;
}
