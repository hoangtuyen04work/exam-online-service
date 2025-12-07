package edu.exam_online.exam_online_system.service.classes;

import edu.exam_online.exam_online_system.dto.request.classes.ChatMessageRequest;
import edu.exam_online.exam_online_system.dto.response.classes.ChatMessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClassMessageService {

    ChatMessageResponse sendMessage(ChatMessageRequest request);

    Page<ChatMessageResponse> getMessages(Long classId, Pageable pageable);

    void updateChatSetting(Long classId, Boolean allowStudentChat);

    Boolean getChatSetting(Long classId);
}
