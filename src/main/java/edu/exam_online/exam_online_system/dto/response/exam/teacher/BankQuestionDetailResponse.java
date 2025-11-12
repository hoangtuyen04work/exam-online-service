package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class BankQuestionDetailResponse {
    private Long bankQuestionId;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
    private List<QuestionResponse> questions;
}