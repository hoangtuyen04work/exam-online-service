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
public class ClassDetailResponse {

    private Long id;
    private String classCode;
    private String name;
    private String description;
    private String semester;
    private String academicYear;
    private Boolean isActive;
    private Long teacherId;
    private String teacherName;
    private String teacherEmail;
    private List<StudentInClassResponse> students;
    private List<ExamSessionInClassResponse> examSessions;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentInClassResponse {
        private Long id;
        private Long studentId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Boolean isActive;
        private OffsetDateTime enrolledAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExamSessionInClassResponse {
        private Long id;
        private Long examSessionId;
        private String examSessionName;
        private String examSessionCode;
        private String description;
        private OffsetDateTime startAt;
        private OffsetDateTime expiredAt;
        private Integer durationMinutes;
        private OffsetDateTime assignedAt;
    }
}
