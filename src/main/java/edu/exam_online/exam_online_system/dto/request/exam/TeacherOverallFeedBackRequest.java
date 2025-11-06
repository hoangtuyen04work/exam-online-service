package edu.exam_online.exam_online_system.dto.request.exam;

import jakarta.validation.Valid;
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
public class TeacherOverallFeedBackRequest {
    private String teacherOverallFeedBack;
    private List<@Valid TeacherFeedBackRequest> teacherFeedBackRequests;
}
