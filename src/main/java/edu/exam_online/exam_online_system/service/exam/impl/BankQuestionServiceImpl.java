package edu.exam_online.exam_online_system.service.exam.impl;

import edu.exam_online.exam_online_system.dto.request.exam.AnswerUpdateRequest;
import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionSearchParam;
import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionUpdateRequest;
import edu.exam_online.exam_online_system.dto.request.exam.QuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.QuestionUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.Answer;
import edu.exam_online.exam_online_system.entity.exam.BankQuestion;
import edu.exam_online.exam_online_system.entity.exam.Question;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.AnswerMapper;
import edu.exam_online.exam_online_system.mapper.BankQuestionMapper;
import edu.exam_online.exam_online_system.mapper.QuestionMapper;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.exam.AnswerRepository;
import edu.exam_online.exam_online_system.repository.exam.BankQuestionRepository;
import edu.exam_online.exam_online_system.repository.exam.QuestionRepository;
import edu.exam_online.exam_online_system.service.exam.BankQuestionService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BankQuestionServiceImpl implements BankQuestionService {

    BankQuestionRepository bankQuestionRepository;
    UserRepository userRepository;
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;

    BankQuestionMapper bankQuestionMapper;
    QuestionMapper questionMapper;
    AnswerMapper answerMapper;

    @Override
    public void deleteById(Long bankQuestionId) {
        bankQuestionRepository.deleteById(bankQuestionId);
    }

    @Override
    public BankQuestionDetailResponse getById(Long bankQuestionId) {
        Long userId = SecurityUtils.getUserId();
        BankQuestion bankQuestion = bankQuestionRepository.findByTeacherIdAndId(userId, bankQuestionId)
                .orElseThrow(() -> new AppException(ErrorCode.BANK_QUESTION_NOT_FOUND));

        return bankQuestionMapper.toDetailResponse(bankQuestion,
                questionMapper.toResponse(bankQuestion.getQuestions()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankQuestionResponse> searchBankQuestion(BankQuestionSearchParam param, Pageable pageable) {
        Long userId = SecurityUtils.getUserId();

        Page<BankQuestion> bankQuestions = bankQuestionRepository.search(userId, param.getName(), pageable);
        return bankQuestions.map(bankQuestionMapper::toResponse);
    }

    @Override
    @Transactional
    public void createBankQuestion(BankQuestionCreationRequest request) {
        BankQuestion bankQuestion = bankQuestionMapper.toEntity(request);
        Long userId = SecurityUtils.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Question> question = request.getQuestions().stream().map(questionRequest -> {
            Question questionEntity = createQuestion(questionRequest);
            questionEntity.setBankQuestion(bankQuestion);
            return questionEntity;
        }).toList();
        bankQuestion.setQuestions(question);
        bankQuestion.setTeacher(user);

        bankQuestionRepository.save(bankQuestion);
        log.info("createBankQuestion:{}", bankQuestion);
    }

    @Override
    @Transactional
    public void updateBankQuestion(Long bankQuestionId, BankQuestionUpdateRequest request) {
        Long userId = SecurityUtils.getUserId();

        BankQuestion bankQuestion = bankQuestionRepository.findByTeacherIdAndId(userId, bankQuestionId)
                .orElseThrow(() -> new AppException(ErrorCode.BANK_QUESTION_NOT_FOUND));

        // Update name and description
        if (request.getName() != null) {
            bankQuestion.setName(request.getName());
        }
        if (request.getDescription() != null) {
            bankQuestion.setDescription(request.getDescription());
        }

        // Update questions if provided
        if (request.getQuestions() != null) {
            updateQuestions(bankQuestionId, request.getQuestions());
        }

        bankQuestionRepository.save(bankQuestion);
        log.info("updateBankQuestion: id={}, name={}", bankQuestionId, request.getName());
    }

    @Override
    @Transactional
    public void updateQuestions(Long bankQuestionId, List<QuestionUpdateRequest> questionRequests) {
        Long userId = SecurityUtils.getUserId();

        // Verify teacher owns the bank question
        BankQuestion bankQuestion = bankQuestionRepository.findByTeacherIdAndId(userId, bankQuestionId)
                .orElseThrow(() -> new AppException(ErrorCode.BANK_QUESTION_NOT_FOUND));

        List<Question> existingQuestions = bankQuestion.getQuestions();

        // Collect question IDs from request
        List<Long> requestQuestionIds = questionRequests.stream()
                .map(QuestionUpdateRequest::getQuestionId)
                .filter(Objects::nonNull)
                .toList();

        // Delete questions that are not in the request
        existingQuestions.stream()
                .filter(question -> !requestQuestionIds.contains(question.getId()))
                .forEach(questionRepository::delete);

        // Clear the list to rebuild
        existingQuestions.clear();

        // Process each question request
        for (QuestionUpdateRequest questionRequest : questionRequests) {
            Question question;
            if (questionRequest.getQuestionId() != null) {
                // Update existing question
                question = questionRepository.findById(questionRequest.getQuestionId())
                        .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

                // Verify question belongs to this bank
                if (!question.getBankQuestion().getId().equals(bankQuestionId)) {
                    throw new AppException(ErrorCode.QUESTION_NOT_FOUND);
                }

                question.setContent(questionRequest.getContent());
                question.setDifficulty(questionRequest.getDifficulty());
                question.setExplanation(questionRequest.getExplanation());
                question.setShuffleAnswers(questionRequest.isShuffleAnswers());
                question.setShuffleQuestions(questionRequest.isShuffleQuestions());

                // Update answers
                updateAnswers(question, questionRequest.getAnswers());
            } else {
                // Create new question
                question = questionMapper.toEntity(questionRequest);
                question.setBankQuestion(bankQuestion);

                // Create answers
                List<Answer> answers = questionRequest.getAnswers().stream()
                        .map(answerRequest -> {
                            return Answer.builder()
                                    .content(answerRequest.getContent())
                                    .isCorrect(answerRequest.isCorrect())
                                    .question(question)
                                    .build();
                        })
                        .toList();
                question.setAnswers(answers);
            }
            existingQuestions.add(question);
        }

        bankQuestionRepository.save(bankQuestion);
        log.info("updateQuestions: bankQuestionId={}, questionsCount={}", bankQuestionId, questionRequests.size());
    }

    private void updateAnswers(Question question, List<AnswerUpdateRequest> answerRequests) {
        List<Answer> existingAnswers = question.getAnswers();

        // Collect answer IDs from request
        List<Long> requestAnswerIds = answerRequests.stream()
                .map(AnswerUpdateRequest::getAnswerId)
                .filter(Objects::nonNull)
                .toList();

        // Delete answers that are not in the request
        existingAnswers.stream()
                .filter(answer -> !requestAnswerIds.contains(answer.getId()))
                .forEach(answerRepository::delete);

        // Clear the list to rebuild
        existingAnswers.clear();

        // Process each answer request
        for (AnswerUpdateRequest answerRequest : answerRequests) {
            Answer answer;
            if (answerRequest.getAnswerId() != null) {
                // Update existing answer
                answer = answerRepository.findById(answerRequest.getAnswerId())
                        .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));
                answer.setContent(answerRequest.getContent());
                answer.setCorrect(answerRequest.isCorrect());
            } else {
                // Create new answer
                answer = Answer.builder()
                        .content(answerRequest.getContent())
                        .isCorrect(answerRequest.isCorrect())
                        .question(question)
                        .build();
            }
            existingAnswers.add(answer);
        }
    }

    private Question createQuestion(QuestionCreationRequest request) {
        Question question = questionMapper.toEntity(request);

        List<Answer> answers = request.getAnswers().stream()
                .map(answerRequest -> answerMapper.toEntity(answerRequest, question))
                .toList();
        question.setAnswers(answers);
        return question;
    }
}
