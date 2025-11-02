package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamSessionStudentSaveRequest {

    @NotNull(message = "Exam session student Id is required")
    private Long examSessionId;

    @NotNull(message = "Questions is required")
    private List<@Valid QuestionSaveRequest> questions;
}
