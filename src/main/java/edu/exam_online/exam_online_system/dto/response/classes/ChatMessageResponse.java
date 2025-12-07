package edu.exam_online.exam_online_system.dto.response.classes;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {

    private Long id;
    private Long classId;
    private Long senderId;
    private String senderName;
    private String senderRole; // TEACHER or STUDENT
    private String content;
    private OffsetDateTime createdAt;
}
