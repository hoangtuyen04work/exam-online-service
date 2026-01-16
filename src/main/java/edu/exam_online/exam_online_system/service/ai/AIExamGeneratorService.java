package edu.exam_online.exam_online_system.service.ai;

import edu.exam_online.exam_online_system.dto.request.ai.AIExamGenerationRequest;
import edu.exam_online.exam_online_system.dto.response.ai.AIExamGenerationResponse;

public interface AIExamGeneratorService {

    /**
     * Generate exam from bank questions using AI
     * 
     * @param request The request containing bank question ID and user requirements
     * @return Generated exam response
     */
    AIExamGenerationResponse generateExamFromBankQuestion(AIExamGenerationRequest request);
}
