package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSessionStatisticsResponse {
    private Long examSessionId;
    private String examSessionName;
    private Integer totalStudents;
    private Integer submittedCount;
    private Integer passedCount;
    private Integer failedCount;
    private Double passingScore;
    private Double averageScore;
    private Double highestScore;
    private Double lowestScore;
    private Double passRate; // percentage
}
