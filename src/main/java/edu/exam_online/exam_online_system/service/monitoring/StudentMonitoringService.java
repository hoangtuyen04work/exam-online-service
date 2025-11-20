package edu.exam_online.exam_online_system.service.monitoring;

import edu.exam_online.exam_online_system.dto.request.websocket.StudentEvent;
import edu.exam_online.exam_online_system.dto.request.websocket.StudentStatusResponse;

import java.security.Principal;
import java.util.List;

public interface StudentMonitoringService {
    List<StudentStatusResponse> getStudentParticipant(Long examSessionId);

    void handleStudentEvent(Principal principal, StudentEvent event, String sessionId);
}
