package edu.exam_online.exam_online_system.controller.websocket;

import edu.exam_online.exam_online_system.commons.constant.StudentEventEnum;
import edu.exam_online.exam_online_system.dto.request.websocket.StudentEvent;
import edu.exam_online.exam_online_system.dto.request.websocket.StudentEventBroadcast;
import edu.exam_online.exam_online_system.service.monitoring.StudentMonitoringService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Controller
public class ClassEventHandlingController {

    StudentMonitoringService studentMonitoringService;

    SimpMessagingTemplate messagingTemplate;

//    @Scheduled(cron = "*/5 * * * * *")
//    public void autoSaveExamSessionStudent() {
//        messagingTemplate.convertAndSend(
//                "/topic/exam/28" ,
//                StudentEventBroadcast.builder()
//                        .userId(1L)
//                        .event(StudentEvent.builder()
//                                .examSessionId(28L)
//                                .event(StudentEventEnum.ENTER).build())
//                        .build());
//    }

    @MessageMapping("/student/event")
    public void handleStudentEvent(
            Principal principal,
            @Payload StudentEvent event,
            @Header("simpSessionId") String sessionId
    ) {
        Long userId = Long.parseLong(principal.getName());

        studentMonitoringService.handleStudentEvent(principal, event, sessionId);

        messagingTemplate.convertAndSend(
                "/topic/exam/" + event.getExamSessionId(),
                StudentEventBroadcast.builder()
                        .userId(userId)
                        .event(event)
                        .build()
        );
    }


    /**
     * Giáo viên gửi tin private cho 1 học sinh
     * /app/teacher/send-private
     */
//    @MessageMapping("/teacher/send-private")
//    public void sendPrivateMessage(
//            Principal principal,
//            @Header("targetUser") String targetUser,
//            @Payload String message
//    ) {
//        // Giáo viên -> student cụ thể
//        messagingTemplate.convertAndSendToUser(targetUser, "/queue/teacher", message);
//    }

}
