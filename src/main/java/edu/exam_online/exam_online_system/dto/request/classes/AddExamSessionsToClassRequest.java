package edu.exam_online.exam_online_system.dto.request.classes;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddExamSessionsToClassRequest {

    @NotEmpty(message = "Exam session IDs list cannot be empty")
    private List<Long> examSessionIds;
}
