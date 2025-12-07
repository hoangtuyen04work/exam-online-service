package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class ExamBankQuestionCreationRequest {

    @NotBlank(message =  "Bank question Id is required")
    private Long bankQuestionId;

    @NotBlank(message =  "Name is required")
    private String name;

    @NotBlank(message =  "Description is required")
    private String description;

    private List<@Valid ExamQuestionLevelRequest> questionLevels;

}
