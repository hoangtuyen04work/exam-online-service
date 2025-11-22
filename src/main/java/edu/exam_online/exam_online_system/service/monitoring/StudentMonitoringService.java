package edu.exam_online.exam_online_system.service.monitoring;

import edu.exam_online.exam_online_system.dto.request.websocket.StudentEvent;
import edu.exam_online.exam_online_system.dto.request.websocket.StudentStatusResponse;
import edu.exam_online.exam_online_system.entity.auth.User;

import java.security.Principal;
import java.util.List;

public interface StudentMonitoringService {
    List<StudentStatusResponse> getStudentParticipant(Long examSessionId);

    User handleStudentEvent(Principal principal, StudentEvent event, String sessionId);
}
