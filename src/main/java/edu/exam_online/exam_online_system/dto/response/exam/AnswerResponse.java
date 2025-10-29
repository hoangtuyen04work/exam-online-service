package edu.exam_online.exam_online_system.dto.response.exam;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponse {
    private Long answerId;
    private String content;
    private boolean correct;
}
