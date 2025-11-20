package edu.exam_online.exam_online_system.dto.request.websocket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentEventBroadcast {
    private Long userId;
    private StudentEvent event;
}
