package edu.exam_online.exam_online_system.dto.response.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentClassResponse {
    private Long classId;
    private String classCode;
    private String name;
    private String description;
    private String semester;
    private String academicYear;
    private String teacherName;
    private Integer totalStudents;
    private Integer totalExamSessions;
    private OffsetDateTime enrolledAt;
}
