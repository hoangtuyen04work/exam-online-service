package edu.exam_online.exam_online_system.dto.request.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIExamGenerationRequest {

    @NotNull(message = "Bank Question ID is required")
    private Long bankQuestionId;

    @NotBlank(message = "Exam name is required")
    private String examName;

    private String examDescription;

    @NotBlank(message = "User requirement is required")
    private String userRequirement;

    // Số lượng câu hỏi mong muốn (tùy chọn)
    private Integer desiredQuestionCount;

    // Độ khó mong muốn: EASY, MEDIUM, HARD, MIXED (tùy chọn)
    private String desiredDifficulty;

    // Thời gian làm bài mong muốn (phút, tùy chọn)
    private Integer desiredDurationMinutes;
}
