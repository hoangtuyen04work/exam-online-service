package edu.exam_online.exam_online_system.dto.request.classes;

import jakarta.validation.constraints.Email;
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
public class AddStudentsToClassRequest {

    @NotEmpty(message = "Student emails list cannot be empty")
    private List<@Email(message = "Invalid email format") String> studentEmails;
}
