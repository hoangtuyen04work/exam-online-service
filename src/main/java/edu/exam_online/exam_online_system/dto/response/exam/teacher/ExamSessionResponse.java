package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSessionResponse {
    private Long examSessionId;
    private String code;
    private String inviteLink;
    private String name;
    private String description;
    private LocalDateTime expiredAt;
    private LocalDateTime startAt;
    private String ownerName;
}

