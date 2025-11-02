package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinExamRequest {
    
    @NotBlank(message = "Exam code is required")
    private String code;
}