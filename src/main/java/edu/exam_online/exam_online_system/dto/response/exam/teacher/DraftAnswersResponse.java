package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DraftAnswersResponse {
    private Long examSessionStudentId;
//    private List<AnswerDto> drafts;
}
