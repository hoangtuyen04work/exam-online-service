package edu.exam_online.exam_online_system.service.ai.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exam_online.exam_online_system.commons.constant.Difficulty;
import edu.exam_online.exam_online_system.config.OpenRouterConfig;
import edu.exam_online.exam_online_system.dto.request.ai.AIExamGenerationRequest;
import edu.exam_online.exam_online_system.dto.response.ai.AIExamGenerationResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.AnswerResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.QuestionResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.*;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.exam.BankQuestionRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamRepository;
import edu.exam_online.exam_online_system.service.ai.AIExamGeneratorService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AIExamGeneratorServiceImpl implements AIExamGeneratorService {

    OpenRouterConfig openRouterConfig;
    RestTemplate restTemplate;
    ObjectMapper objectMapper;
    BankQuestionRepository bankQuestionRepository;
    ExamRepository examRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public AIExamGenerationResponse generateExamFromBankQuestion(AIExamGenerationRequest request) {
        Long userId = SecurityUtils.getUserId();

        // 1. Lấy ngân hàng câu hỏi
        BankQuestion bankQuestion = bankQuestionRepository.findByIdAndTeacherId(request.getBankQuestionId(), userId)
                .orElseThrow(() -> new AppException(ErrorCode.BANK_QUESTION_NOT_FOUND));

        // 2. Chuẩn bị dữ liệu câu hỏi để gửi cho AI
        List<Question> allQuestions = bankQuestion.getQuestions();
        if (allQuestions == null || allQuestions.isEmpty()) {
            throw new AppException(ErrorCode.BANK_QUESTION_NOT_FOUND);
        }

        // 3. Tạo prompt cho AI
        String prompt = buildPrompt(allQuestions, request);

        // 4. Gọi OpenRouter API
        String aiResponse = callOpenRouterAPI(prompt);

        // 5. Parse response từ AI
        List<Long> selectedQuestionIds = parseAIResponse(aiResponse, allQuestions);

        // 6. Lấy các câu hỏi được chọn
        List<Question> selectedQuestions = allQuestions.stream()
                .filter(q -> selectedQuestionIds.contains(q.getId()))
                .collect(Collectors.toList());

        // Nếu AI không chọn được câu nào hoặc chọn ít hơn mong muốn, bổ sung random
        if (selectedQuestions.isEmpty() || (request.getDesiredQuestionCount() != null
                && selectedQuestions.size() < request.getDesiredQuestionCount())) {
            selectedQuestions = selectQuestionsWithFallback(allQuestions, request, selectedQuestions);
        }

        // 7. Tạo Exam mới
        User teacher = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Exam exam = Exam.builder()
                .name(request.getExamName())
                .description(request.getExamDescription())
                .teacher(teacher)
                .build();

        Set<QuestionExam> questionExams = new HashSet<>();
        int order = 0;
        float totalPoints = 0f;

        for (Question question : selectedQuestions) {
            QuestionExam qe = QuestionExam.builder()
                    .exam(exam)
                    .question(question)
                    .point(1.0f) // Mặc định 1 điểm/câu
                    .orderColumn(order++)
                    .build();
            questionExams.add(qe);
            totalPoints += 1.0f;
        }

        exam.setQuestionExams(questionExams);
        teacher.getExams().add(exam);

        Exam savedExam = examRepository.save(exam);

        // 8. Tạo response
        List<QuestionResponse> questionResponses = selectedQuestions.stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());

        // Lấy suggestion từ AI response
        String aiSuggestion = extractAISuggestion(aiResponse);

        return AIExamGenerationResponse.builder()
                .examId(savedExam.getId())
                .examName(savedExam.getName())
                .examDescription(savedExam.getDescription())
                .totalQuestions(selectedQuestions.size())
                .totalPoints(totalPoints)
                .aiSuggestion(aiSuggestion)
                .questions(questionResponses)
                .build();
    }

    private String buildPrompt(List<Question> questions, AIExamGenerationRequest request) {
        StringBuilder sb = new StringBuilder();

        sb.append("Bạn là một trợ lý AI giúp giáo viên tạo đề thi từ ngân hàng câu hỏi.\n\n");
        sb.append("YÊU CẦU CỦA GIÁO VIÊN:\n");
        sb.append(request.getUserRequirement()).append("\n\n");

        if (request.getDesiredQuestionCount() != null) {
            sb.append("Số câu hỏi mong muốn: ").append(request.getDesiredQuestionCount()).append("\n");
        }
        if (request.getDesiredDifficulty() != null) {
            sb.append("Độ khó mong muốn: ").append(request.getDesiredDifficulty()).append("\n");
        }
        if (request.getDesiredDurationMinutes() != null) {
            sb.append("Thời gian làm bài: ").append(request.getDesiredDurationMinutes()).append(" phút\n");
        }

        sb.append("\nNGÂN HÀNG CÂU HỎI:\n");
        sb.append("-------------------\n");

        for (Question q : questions) {
            sb.append("ID: ").append(q.getId()).append("\n");
            sb.append("Nội dung: ").append(q.getContent()).append("\n");
            sb.append("Độ khó: ").append(q.getDifficulty() != null ? q.getDifficulty().name() : "MEDIUM").append("\n");

            if (q.getAnswers() != null && !q.getAnswers().isEmpty()) {
                sb.append("Đáp án: ");
                for (Answer a : q.getAnswers()) {
                    sb.append("[").append(a.getContent());
                    if (a.isCorrect())
                        sb.append(" (đúng)");
                    sb.append("] ");
                }
                sb.append("\n");
            }
            sb.append("---\n");
        }

        sb.append("\nYÊU CẦU PHẢN HỒI:\n");
        sb.append("Hãy phân tích yêu cầu của giáo viên và chọn các câu hỏi phù hợp từ ngân hàng.\n");
        sb.append("Trả về JSON với format:\n");
        sb.append("{\n");
        sb.append("  \"selectedQuestionIds\": [1, 2, 3, ...],\n");
        sb.append("  \"suggestion\": \"Lời khuyên và giải thích về đề thi đã tạo\"\n");
        sb.append("}\n");
        sb.append("CHỈ trả về JSON, không có text khác.\n");

        return sb.toString();
    }

    private String callOpenRouterAPI(String prompt) {
        try {
            String apiKey = openRouterConfig.getApiKey();
            String apiUrl = openRouterConfig.getApiUrl();
            String model = openRouterConfig.getModel();

            if (apiKey == null || apiKey.isEmpty()) {
                log.warn("OpenRouter API key not configured, using fallback selection");
                return null;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            headers.set("HTTP-Referer", "https://exam-online-system.edu.vn");
            headers.set("X-Title", "Exam Online System");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);

            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2000);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode choices = root.path("choices");
                if (choices.isArray() && choices.size() > 0) {
                    return choices.get(0).path("message").path("content").asText();
                }
            }

            log.warn("OpenRouter API returned unexpected response");
            return null;

        } catch (Exception e) {
            log.error("Error calling OpenRouter API: {}", e.getMessage());
            return null;
        }
    }

    private List<Long> parseAIResponse(String aiResponse, List<Question> allQuestions) {
        List<Long> questionIds = new ArrayList<>();

        if (aiResponse == null || aiResponse.isEmpty()) {
            return questionIds;
        }

        try {
            // Tìm JSON trong response
            String jsonContent = extractJsonFromResponse(aiResponse);

            JsonNode root = objectMapper.readTree(jsonContent);
            JsonNode selectedIds = root.path("selectedQuestionIds");

            if (selectedIds.isArray()) {
                for (JsonNode id : selectedIds) {
                    questionIds.add(id.asLong());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse AI response: {}", e.getMessage());
        }

        return questionIds;
    }

    private String extractJsonFromResponse(String response) {
        // Tìm vị trí bắt đầu và kết thúc của JSON
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start) {
            return response.substring(start, end + 1);
        }

        return response;
    }

    private String extractAISuggestion(String aiResponse) {
        if (aiResponse == null || aiResponse.isEmpty()) {
            return "Đề thi đã được tạo dựa trên yêu cầu của bạn.";
        }

        try {
            String jsonContent = extractJsonFromResponse(aiResponse);
            JsonNode root = objectMapper.readTree(jsonContent);
            String suggestion = root.path("suggestion").asText();

            if (suggestion != null && !suggestion.isEmpty()) {
                return suggestion;
            }
        } catch (Exception e) {
            log.warn("Failed to extract suggestion: {}", e.getMessage());
        }

        return "Đề thi đã được tạo dựa trên yêu cầu của bạn.";
    }

    private List<Question> selectQuestionsWithFallback(List<Question> allQuestions, AIExamGenerationRequest request,
            List<Question> alreadySelected) {
        List<Question> result = new ArrayList<>(alreadySelected);
        int targetCount = request.getDesiredQuestionCount() != null ? request.getDesiredQuestionCount()
                : Math.min(10, allQuestions.size());

        Set<Long> selectedIds = result.stream().map(Question::getId).collect(Collectors.toSet());

        // Lọc theo độ khó nếu có yêu cầu
        List<Question> candidates = allQuestions.stream()
                .filter(q -> !selectedIds.contains(q.getId()))
                .collect(Collectors.toList());

        if (request.getDesiredDifficulty() != null && !request.getDesiredDifficulty().equalsIgnoreCase("MIXED")) {
            try {
                Difficulty desiredDiff = Difficulty.valueOf(request.getDesiredDifficulty().toUpperCase());
                List<Question> filtered = candidates.stream()
                        .filter(q -> q.getDifficulty() == desiredDiff)
                        .collect(Collectors.toList());
                if (!filtered.isEmpty()) {
                    candidates = filtered;
                }
            } catch (Exception e) {
                // Ignore invalid difficulty
            }
        }

        // Random shuffle và chọn
        Collections.shuffle(candidates);

        for (Question q : candidates) {
            if (result.size() >= targetCount)
                break;
            result.add(q);
        }

        return result;
    }

    private QuestionResponse mapToQuestionResponse(Question question) {
        List<AnswerResponse> answerResponses = new ArrayList<>();

        if (question.getAnswers() != null) {
            for (Answer answer : question.getAnswers()) {
                answerResponses.add(AnswerResponse.builder()
                        .answerId(answer.getId())
                        .content(answer.getContent())
                        .correct(answer.isCorrect())
                        .build());
            }
        }

        return QuestionResponse.builder()
                .questionId(question.getId())
                .content(question.getContent())
                .difficulty(question.getDifficulty())
                .explanation(question.getExplanation())
                .shuffleAnswers(question.isShuffleAnswers())
                .shuffleQuestions(question.isShuffleQuestions())
                .answers(answerResponses)
                .point(1.0f)
                .build();
    }
}
