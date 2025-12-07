package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.commons.PageResponse;
import edu.exam_online.exam_online_system.dto.request.classes.ChatMessageRequest;
import edu.exam_online.exam_online_system.dto.response.classes.ChatMessageResponse;
import edu.exam_online.exam_online_system.service.classes.ClassMessageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ClassChatController {

    ClassMessageService classMessageService;
    SimpMessagingTemplate messagingTemplate;

    @PostMapping("/{classId}/messages")
    @Operation(summary = "Send a message in class chat")
    public BaseResponse<ChatMessageResponse> sendMessage(
            @PathVariable Long classId,
            @Valid @RequestBody ChatMessageRequest request) {
        request.setClassId(classId);
        ChatMessageResponse response = classMessageService.sendMessage(request);

        // Send to WebSocket subscribers
        messagingTemplate.convertAndSend("/topic/class/" + classId + "/chat", response);

        return BaseResponse.success(response);
    }

    @GetMapping("/{classId}/messages")
    @Operation(summary = "Get messages from class chat")
    public PageResponse<ChatMessageResponse> getMessages(
            @PathVariable Long classId,
            @ParameterObject Pageable pageable) {
        return PageResponse.success(classMessageService.getMessages(classId, pageable));
    }

    @PutMapping("/{classId}/chat-settings")
    @Operation(summary = "Update chat settings (teacher only)")
    public BaseResponse<Void> updateChatSettings(
            @PathVariable Long classId,
            @RequestParam Boolean allowStudentChat) {
        classMessageService.updateChatSetting(classId, allowStudentChat);
        return BaseResponse.success();
    }

    @GetMapping("/{classId}/chat-settings")
    @Operation(summary = "Get chat settings")
    public BaseResponse<Boolean> getChatSettings(@PathVariable Long classId) {
        return BaseResponse.success(classMessageService.getChatSetting(classId));
    }

    // WebSocket endpoint
    @MessageMapping("/class/{classId}/chat")
    @SendTo("/topic/class/{classId}/chat")
    public ChatMessageResponse sendMessageWS(@Payload ChatMessageRequest request) {
        return classMessageService.sendMessage(request);
    }
}
