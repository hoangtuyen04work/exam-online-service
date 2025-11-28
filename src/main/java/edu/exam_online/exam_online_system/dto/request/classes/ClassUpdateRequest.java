package edu.exam_online.exam_online_system.dto.request.classes;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassUpdateRequest {

    @Size(max = 100, message = "Class name must not exceed 100 characters")
    private String name;

    private String description;


}
