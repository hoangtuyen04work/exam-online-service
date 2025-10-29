package edu.exam_online.exam_online_system.dto.request.exam;

import edu.exam_online.exam_online_system.commons.constant.Difficulty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionUpdateRequest {
    private Long questionId;

    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    private String content;

    @NotNull(message = "Độ khó của câu hỏi không được để trống")
    private Difficulty difficulty;

    @NotBlank(message = "Giải thích không được để trống")
    private String explanation;

    // Shuffle có thể tùy chọn, không cần validate
    private boolean shuffleAnswers;
    private boolean shuffleQuestions;

    @NotNull(message = "Điểm cho câu hỏi không được để trống")
    @Positive(message = "Điểm phải lớn hơn 0")
    private Float point;

    @NotNull(message = "Thứ tự câu hỏi không được để trống")
    @Positive(message = "Thứ tự câu hỏi phải lớn hơn 0")
    private Integer orderColumn;

    @NotNull(message = "Danh sách đáp án không được để trống")
    @Size(min = 2, message = "Mỗi câu hỏi phải có ít nhất 2 đáp án")
    private List<@Valid AnswerUpdateRequest> answers;

}