package edu.exam_online.exam_online_system.dto.response.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentClassDetailResponse {
    private Long classId;
    private String classCode;
    private String name;
    private String description;
    private String semester;
    private String academicYear;
    private String teacherName;
    private String teacherEmail;
    private Integer totalStudents;
    private List<ExamSessionInfo> examSessions;
    private OffsetDateTime enrolledAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExamSessionInfo {
        private Long classExamSessionId;
        private Long examSessionId;
        private String examSessionName;
        private String examSessionCode;
        private String description;
        private OffsetDateTime startAt;
        private OffsetDateTime expiredAt;
        private Integer durationMinutes;
        private OffsetDateTime assignedAt;
        private String inviteLink;
    }
}
