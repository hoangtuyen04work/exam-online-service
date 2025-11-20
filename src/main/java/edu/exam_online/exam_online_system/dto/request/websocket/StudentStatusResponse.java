package edu.exam_online.exam_online_system.dto.request.websocket;

import edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentStatusResponse {
    private Long userId;
    private String username;
    private ExamStudentStatusEnum status;
}
