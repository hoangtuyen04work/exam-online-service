package edu.exam_online.exam_online_system.dto.response.exam.student.result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResultResponse {
    private Long answerId;
    private String content;
    private boolean correct;
    private boolean selected;
}
