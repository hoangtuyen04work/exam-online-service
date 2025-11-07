package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class BankQuestionResponse {
    private Long bankQuestionId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}