package edu.exam_online.exam_online_system.service.exam.impl;

import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionUpdateRequest;
import edu.exam_online.exam_online_system.dto.request.param.ExamSessionSearchParam;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamSessionResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamSessionStatisticsResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.*;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.ExamSessionMapper;
import edu.exam_online.exam_online_system.repository.ExamSessionAnswerSnapshotRepository;
import edu.exam_online.exam_online_system.repository.ExamSessionQuestionSnapshotRepository;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamSessionRepository;
import edu.exam_online.exam_online_system.service.exam.ExamSessionService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ExamSessionServiceImpl implements ExamSessionService {
    ExamSessionRepository examSessionRepository;
    UserRepository userRepository;
    ExamRepository examRepository;
    ExamSessionQuestionSnapshotRepository examSessionQuestionSnapshotRepository;
    ExamSessionAnswerSnapshotRepository examSessionAnswerSnapshotRepository;

    ExamSessionMapper examSessionMapper;

    static String WEB_DOMAIN = "http://localhost:3000/exam/";

    @Transactional
    @Override
    public ExamSessionResponse createSession(ExamSessionCreationRequest request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        Long ownerId = SecurityUtils.getUserId();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String code = generateExamSessionCode();
        String inviteUrl = generateInviteExamSession(code);
        ExamSession session = examSessionMapper.toEntity(request, exam, owner, code);

        examSessionRepository.save(session);

        // Create snapshot of questions and answers
        createExamSessionSnapshot(session, exam);

        return examSessionMapper.toResponse(session, inviteUrl);
    }

    /**
     * Create snapshot of exam questions and answers at the time of exam session
     * creation
     * This prevents changes to the original exam from affecting active exam
     * sessions
     */
    private void createExamSessionSnapshot(ExamSession session, Exam exam) {
        log.info("Creating snapshot for exam session {} from exam {}", session.getId(), exam.getId());

        int questionOrder = 0;
        for (QuestionExam questionExam : exam.getQuestionExams()) {
            Question originalQuestion = questionExam.getQuestion();

            // Create snapshot of question
            ExamSessionQuestionSnapshot snapshotQuestion = ExamSessionQuestionSnapshot.builder()
                    .examSession(session)
                    .originalQuestionId(originalQuestion.getId())
                    .content(originalQuestion.getContent())
                    .shuffleAnswers(originalQuestion.isShuffleAnswers())
                    .shuffleQuestions(originalQuestion.isShuffleQuestions())
                    .difficulty(originalQuestion.getDifficulty())
                    .explanation(originalQuestion.getExplanation())
                    .questionOrder(questionOrder++)
                    .build();

            examSessionQuestionSnapshotRepository.save(snapshotQuestion);

            // Create snapshot of answers
            int answerOrder = 0;
            for (Answer originalAnswer : originalQuestion.getAnswers()) {
                ExamSessionAnswerSnapshot snapshotAnswer = ExamSessionAnswerSnapshot.builder()
                        .examSessionQuestionSnapshot(snapshotQuestion)
                        .originalAnswerId(originalAnswer.getId())
                        .content(originalAnswer.getContent())
                        .isCorrect(originalAnswer.isCorrect())
                        .answerOrder(answerOrder++)
                        .build();

                examSessionAnswerSnapshotRepository.save(snapshotAnswer);
            }
        }

        log.info("Successfully created snapshot with {} questions for exam session {}",
                questionOrder, session.getId());
    }

    @Override
    @Transactional
    public ExamSessionResponse updateSession(Long examSessionId, ExamSessionUpdateRequest request) {
        Long userId = SecurityUtils.getUserId();
        ExamSession session = examSessionRepository.findByIdAndOwnerId(examSessionId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_NOT_FOUND));

        examSessionMapper.updateEntity(session, request);
        session = examSessionRepository.save(session);
        return examSessionMapper.toResponse(session, generateInviteExamSession(session.getCode()));
    }

    @Override
    public void deleteSession(Long examSessionId) {
        Long userId = SecurityUtils.getUserId();
        ExamSession session = examSessionRepository.findByIdAndOwnerId(examSessionId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_NOT_FOUND));

        examSessionRepository.delete(session);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamSessionResponse> getAll(ExamSessionSearchParam param, Pageable pageable) {
        Long userId = SecurityUtils.getUserId();
        Page<ExamSession> examSessions = examSessionRepository.findAllByOwnerIdOrderByCreatedAtDesc(param.getExamId(),
                userId, pageable);
        return examSessions.map(
                examSession -> examSessionMapper.toResponse(examSession,
                        generateInviteExamSession(examSession.getCode())));
    }

    @Override
    public ExamSessionResponse getDetail(Long examSessionId) {
        ExamSession session = examSessionRepository.findById(examSessionId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        return examSessionMapper.toResponse(session, generateInviteExamSession(session.getCode()));
    }

    private String generateExamSessionCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    @Transactional
    public ExamSessionResponse updatePassingScore(Long examSessionId, Double passingScore) {
        Long userId = SecurityUtils.getUserId();
        ExamSession session = examSessionRepository.findByIdAndOwnerId(examSessionId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_NOT_FOUND));

        session.setPassingScore(passingScore);
        session = examSessionRepository.save(session);
        return examSessionMapper.toResponse(session, generateInviteExamSession(session.getCode()));
    }

    @Override
    @Transactional(readOnly = true)
    public ExamSessionStatisticsResponse getExamSessionStatistics(Long examSessionId) {
        Long userId = SecurityUtils.getUserId();
        ExamSession session = examSessionRepository.findByIdAndOwnerId(examSessionId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_NOT_FOUND));

        int totalStudents = session.getExamSessionStudents().size();
        int submittedCount = 0;
        int passedCount = 0;
        int failedCount = 0;
        double totalScore = 0;
        Double highestScore = null;
        Double lowestScore = null;

        for (ExamSessionStudent examSessionStudent : session.getExamSessionStudents()) {

            submittedCount++;
            double score = examSessionStudent.getTotalScore() != null ? examSessionStudent.getTotalScore() : 0;
            totalScore += score;

            if (highestScore == null || score > highestScore) {
                highestScore = score;
            }
            if (lowestScore == null || score < lowestScore) {
                lowestScore = score;
            }

            if (session.getPassingScore() != null) {
                if (score >= session.getPassingScore()) {
                    passedCount++;
                } else {
                    failedCount++;
                }
            }

        }

        double averageScore = submittedCount > 0 ? totalScore / submittedCount : 0;
        double passRate = submittedCount > 0 ? (passedCount * 100.0 / submittedCount) : 0;

        return edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamSessionStatisticsResponse.builder()
                .examSessionId(session.getId())
                .examSessionName(session.getName())
                .totalStudents(totalStudents)
                .submittedCount(submittedCount)
                .passedCount(passedCount)
                .failedCount(failedCount)
                .passingScore(session.getPassingScore())
                .averageScore(averageScore)
                .highestScore(highestScore)
                .lowestScore(lowestScore)
                .passRate(passRate)
                .build();
    }

    private String generateInviteExamSession(String code) {
        return WEB_DOMAIN + "join/" + code;
    }
}
