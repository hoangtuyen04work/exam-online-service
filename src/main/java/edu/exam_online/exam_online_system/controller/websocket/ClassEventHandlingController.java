package edu.exam_online.exam_online_system.controller.websocket;

import edu.exam_online.exam_online_system.dto.request.websocket.StudentEvent;
import edu.exam_online.exam_online_system.dto.request.websocket.StudentEventBroadcast;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.service.monitoring.StudentMonitoringService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Controller
public class ClassEventHandlingController {

    StudentMonitoringService studentMonitoringService;

    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/student/event")
    public void handleStudentEvent(
            Principal principal,
            @Payload StudentEvent event,
            @Header("simpSessionId") String sessionId
    ) {
        Long userId = Long.parseLong(principal.getName());

        User user = studentMonitoringService.handleStudentEvent(principal, event, sessionId);

        messagingTemplate.convertAndSend(
                "/topic/exam/" + event.getExamSessionId(),
                StudentEventBroadcast.builder()
                        .userId(userId)
                        .username(user.getUsername())
                        .event(event)
                        .build()
        );
    }

}
