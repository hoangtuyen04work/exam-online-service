package edu.exam_online.exam_online_system.dto.request.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankQuestionRequest {
    private Long bankQuestionId;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
}