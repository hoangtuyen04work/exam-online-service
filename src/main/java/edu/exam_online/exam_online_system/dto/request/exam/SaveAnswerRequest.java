package edu.exam_online.exam_online_system.dto.request.exam;

import edu.exam_online.exam_online_system.commons.constant.ExamSubmitStateEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveAnswerRequest {

    @NotNull(message = "Exam session student Id is required")
    private Long examSessionStudentId;

    @NotNull(message = "State is required")
    private ExamSubmitStateEnum state;

    @NotNull(message = "Answer Id is required")
    private List<@Valid SaveQuestionRequest> questions;
}
