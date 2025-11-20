// StudentEvent.java
package edu.exam_online.exam_online_system.dto.request.websocket;

import edu.exam_online.exam_online_system.commons.constant.StudentEventEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentEvent {

    @NotBlank(message = "ExamSessionId is required")
    private Long examSessionId;

    @NotBlank(message = "Type is required")
    private StudentEventEnum event;
//    private String payload;
}
