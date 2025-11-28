package edu.exam_online.exam_online_system.dto.request.classes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCreationRequest {

    @NotBlank(message = "Class name is required")
    @Size(max = 100, message = "Class name must not exceed 100 characters")
    private String name;

    private String description;

    @Size(max = 20, message = "Semester must not exceed 20 characters")
    private String semester;

    @Size(max = 20, message = "Academic year must not exceed 20 characters")
    private String academicYear;
}
