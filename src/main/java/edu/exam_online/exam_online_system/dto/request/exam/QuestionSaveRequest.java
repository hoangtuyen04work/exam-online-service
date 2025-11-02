package edu.exam_online.exam_online_system.dto.request.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionSaveRequest {
    Long questionId;
    Long answerId;
}
