package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePassingScoreRequest {

    @NotNull(message = "Passing score is required")
    @Min(value = 0, message = "Passing score must be greater than or equal to 0")
    private Double passingScore;
}
