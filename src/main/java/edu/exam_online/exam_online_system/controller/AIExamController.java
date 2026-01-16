package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.dto.request.ai.AIExamGenerationRequest;
import edu.exam_online.exam_online_system.dto.response.ai.AIExamGenerationResponse;
import edu.exam_online.exam_online_system.service.ai.AIExamGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher/ai")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Tag(name = "AI Exam Generator", description = "APIs for AI-powered exam generation")
public class AIExamController {

    AIExamGeneratorService aiExamGeneratorService;

    @PostMapping("/generate-exam")
    @Operation(summary = "Generate exam from bank questions using AI", description = "Uses AI to analyze questions from a bank and create an exam based on user requirements")
    public BaseResponse<AIExamGenerationResponse> generateExamFromBankQuestion(
            @Valid @RequestBody AIExamGenerationRequest request) {
        return BaseResponse.success(aiExamGeneratorService.generateExamFromBankQuestion(request));
    }
}
