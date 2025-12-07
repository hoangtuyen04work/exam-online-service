package edu.exam_online.exam_online_system.service.exam.impl;

import edu.exam_online.exam_online_system.commons.constant.ExamSubmitStateEnum;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionStudentSaveRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExitEventRequest;
import edu.exam_online.exam_online_system.dto.request.exam.JoinExamRequest;
import edu.exam_online.exam_online_system.dto.request.exam.QuestionSaveRequest;
import edu.exam_online.exam_online_system.dto.request.exam.TeacherOverallFeedBackRequest;
import edu.exam_online.exam_online_system.dto.response.exam.student.ExamSessionContentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.ExamSessionStudentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.JoinExamSessionResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.result.ExamSessionStudentResultResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.StudentJoinedExamSessionResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.ExamSession;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionAnswerSnapshot;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionQuestionSnapshot;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudent;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudentAnswer;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.ExamSessionStudentAnswerMapper;
import edu.exam_online.exam_online_system.mapper.ExamSessionStudentMapper;
import edu.exam_online.exam_online_system.repository.ExamSessionQuestionSnapshotRepository;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamSessionRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamSessionStudentRepository;
import edu.exam_online.exam_online_system.service.exam.ExamSessionStudentService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import edu.exam_online.exam_online_system.utils.TimeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static edu.exam_online.exam_online_system.commons.constant.ExamSessionStudentStateEnum.CLOSED;
import static edu.exam_online.exam_online_system.commons.constant.ExamSessionStudentStateEnum.JOINED;
import static edu.exam_online.exam_online_system.commons.constant.ExamSessionStudentStateEnum.NOT_OPEN;
import static edu.exam_online.exam_online_system.commons.constant.ExamSessionStudentStateEnum.OPENING;
import static edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum.COMPLETED;
import static edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum.IN_PROGRESS;
import static edu.exam_online.exam_online_system.commons.constant.RoleEnum.STUDENT;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ExamSessionStudentServiceImpl implements ExamSessionStudentService {

    ExamSessionRepository examSessionRepository;
    ExamSessionStudentRepository examSessionStudentRepository;
    ExamSessionQuestionSnapshotRepository examSessionQuestionSnapshotRepository;
    UserRepository userRepository;

    ExamSessionStudentMapper examSessionStudentMapper;
    ExamSessionStudentAnswerMapper examSessionStudentAnswerMapper;

    @Override
    @Transactional
    public void teacherFeedBack(Long examSessionStudentId, TeacherOverallFeedBackRequest request) {
        ExamSessionStudent examSessionStudent = examSessionStudentRepository.findById(examSessionStudentId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_STUDENT_NOT_FOUND));

        examSessionStudentMapper.updateEntity(examSessionStudent, request);
        examSessionStudentRepository.save(examSessionStudent);
    }

    @Override
    public Page<StudentJoinedExamSessionResponse> getStudentJoinedExamSession(Long examSessionId, Pageable pageable) {
        Page<ExamSessionStudent> examSessionStudents = examSessionStudentRepository
                .findByExamSessionIdOrderByCreatedAtDesc(examSessionId, pageable);
        return examSessionStudents.map(student -> {
            StudentJoinedExamSessionResponse response = examSessionStudentMapper.toJoinedResponse(student);
            // Calculate isPassed based on passing score
            Double passingScore = student.getExamSession().getPassingScore();
            if (passingScore != null) {
                response.setIsPassed(response.getScore() >= passingScore);
            }
            return response;
        });
    }

    @Override
    @Transactional
    public ExamSessionStudentResultResponse getResultByExamSessionId(Long examSessionId) {
        Long studentId = SecurityUtils.getUserId();
        ExamSessionStudent examSessionStudent = examSessionStudentRepository
                .findByExamSessionIdAndStudentId(examSessionId, studentId);
        if (examSessionStudent == null) {
            throw new AppException(ErrorCode.EXAM_SESSION_STUDENT_NOT_FOUND);
        }
        ExamSessionStudentResultResponse response = examSessionStudentMapper.toResponse(examSessionStudent);

        // Calculate isPassed based on passing score
        Double passingScore = examSessionStudent.getExamSession().getPassingScore();
        response.setPassingScore(passingScore);
        if (passingScore != null && response.getTotalScore() != null) {
            response.setIsPassed(response.getTotalScore() >= passingScore);
        }

        return response;
    }

    @Override
    public ExamSessionStudentResultResponse getExamSessionResultByExamSessionStudentId(Long examSessionStudentId) {
        ExamSessionStudent examSessionStudent = examSessionStudentRepository.findById(examSessionStudentId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_STUDENT_NOT_FOUND));

        ExamSessionStudentResultResponse response = examSessionStudentMapper.toResponse(examSessionStudent);
        // Calculate isPassed based on passing score
        Double passingScore = examSessionStudent.getExamSession().getPassingScore();
        response.setPassingScore(passingScore);
        if (passingScore != null && response.getTotalScore() != null) {
            response.setIsPassed(response.getTotalScore() >= passingScore);
        }
        return response;
    }

    @Override
    @Transactional
    public void autoSaveExamSessionStudent() {
        List<ExamSessionStudent> examSessionStudents = examSessionStudentRepository.findExpiredExamSessionStudent();
        examSessionStudents.forEach(examSessionStudent -> {
            examSessionStudent.setSubmittedAt(examSessionStudent.getExpiredAt());
            examSessionStudent.setStatus(COMPLETED);
        });
        examSessionStudentRepository.saveAll(examSessionStudents);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamSessionStudentResponse> searchExamSession(Pageable pageable) {
        Long studentId = SecurityUtils.getUserId();
        Page<ExamSessionStudent> resultPage = examSessionStudentRepository
                .findByStudentIdAndStatusOrderByCreatedAtDesc(studentId, COMPLETED, pageable);
        return resultPage.map(entity -> {
            ExamSessionStudentResponse dto = examSessionStudentMapper.toDto(entity);
            examSessionStudentMapper.toDto(entity);
            if (dto.getTotalScore() == null) {
                dto.setTotalScore(computeFallbackScore(entity));
            }
            return dto;
        });
    }

    @Override
    @Transactional
    public void saveExitEvent(ExitEventRequest request) {
        ExamSessionStudent examSessionStudent = examSessionStudentRepository.findById(request.getExamSessionStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_STUDENT_NOT_FOUND));

        examSessionStudent.setExitCount(examSessionStudent.getExitCount() + 1);
        examSessionStudentRepository.save(examSessionStudent);
    }

    @Override
    @Transactional
    public boolean saveExam(ExamSubmitStateEnum state, ExamSessionStudentSaveRequest request) {
        Long studentId = SecurityUtils.getUserId();
        ExamSessionStudent examSessionStudent = examSessionStudentRepository
                .findByExamSessionIdAndStudentId(request.getExamSessionId(), studentId);
        if (examSessionStudent.getExpiredAt().isBefore(TimeUtils.getCurrentTime())
                || examSessionStudent.getStatus().equals(COMPLETED)) {
            return false;
        }
        if (state == ExamSubmitStateEnum.DRAFT) {
            saveDraftExamSessionStudentAnswer(examSessionStudent, request);
        } else if (state == ExamSubmitStateEnum.FINAL) {
            saveFinalExamSessionStudentAnswer(examSessionStudent, request);
        }
        return true;
    }

    @Override
    @Transactional
    public ExamSessionContentResponse doExam(Long examSessionId) {
        ExamSession examSession = examSessionRepository.findById(examSessionId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_NOT_FOUND));
        Long studentId = SecurityUtils.getUserId();

        validateExamSession(examSession);
        ExamSessionStudent examSessionStudent = examSessionStudentRepository
                .findByExamSessionIdAndStudentId(examSession.getId(), studentId);

        if (examSessionStudent == null) {
            return newExamSessionStudent(examSession, studentId);
        } else if (examSessionStudent.getStatus().equals(IN_PROGRESS)) {
            return draftExamSessionStudent(examSessionStudent);
        } else {
            return doneExamSessionStudent(examSessionStudent);
        }
    }

    @Override
    public JoinExamSessionResponse joinExam(JoinExamRequest request) {

        Long studentId = SecurityUtils.getUserId();
        User student = userRepository.findByIdAndRoleName(studentId, STUDENT.name())
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_JOIN_EXAM_SESSION));

        ExamSession examSession = examSessionRepository.findByCode(request.getCode())
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_NOT_FOUND));

        return joinExam(examSession, student);

    }

    private JoinExamSessionResponse joinExam(ExamSession examSession, User student) {
        OffsetDateTime now = TimeUtils.getCurrentTime();

        if (now.isBefore(examSession.getStartAt())) {
            return examSessionStudentMapper.toResponse(examSession, NOT_OPEN);
        } else if (examSession.getExpiredAt() != null && now.isAfter(examSession.getExpiredAt())) {
            return examSessionStudentMapper.toResponse(examSession, CLOSED);
        } else if (examSessionStudentRepository.existsByExamSessionIdAndStudentId(examSession.getId(),
                student.getId())) {
            return examSessionStudentMapper.toResponse(examSession, JOINED);
        } else {
            return examSessionStudentMapper.toResponse(examSession, OPENING);
        }
    }

    private void validateExamSession(ExamSession examSession) {
        OffsetDateTime now = TimeUtils.getCurrentTime();
        if (now.isBefore(examSession.getStartAt())) {
            throw new AppException(ErrorCode.EXAM_NOT_YET_START);
        }

        if (examSession.getExpiredAt() != null && now.isAfter(examSession.getExpiredAt())) {
            throw new AppException(ErrorCode.EXAM_CLOSED);
        }
    }

    private ExamSessionContentResponse doneExamSessionStudent(ExamSessionStudent examSessionStudent) {
        return examSessionStudentMapper.toDoneResponse(examSessionStudent, COMPLETED);
    }

    private ExamSessionContentResponse draftExamSessionStudent(ExamSessionStudent examSessionStudent) {
        return examSessionStudentMapper.toDraftResponse(examSessionStudent, IN_PROGRESS);
    }

    private ExamSessionContentResponse newExamSessionStudent(ExamSession examSession, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ExamSessionStudent examSessionStudent = examSessionStudentMapper.toEntity(examSession, student);

        // Use snapshot questions from exam session
        List<ExamSessionQuestionSnapshot> snapshotQuestions = examSessionQuestionSnapshotRepository
                .findByExamSessionIdWithAnswers(examSession.getId());

        if (snapshotQuestions.isEmpty()) {
            throw new AppException(ErrorCode.EXAM_SESSION_SNAPSHOT_NOT_FOUND);
        }

        // Create student answers for each snapshot question
        List<ExamSessionStudentAnswer> answers = snapshotQuestions.stream()
                .map(snapshotQuestion -> examSessionStudentAnswerMapper.toEntityFromSnapshot(OPENING,
                        examSessionStudent,
                        snapshotQuestion))
                .toList();
        examSessionStudent.setAnswers(answers);

        ExamSessionStudent saved = examSessionStudentRepository.save(examSessionStudent);

        return examSessionStudentMapper.toNewResponse(saved);
    }

    private void saveDraftExamSessionStudentAnswer(ExamSessionStudent examSessionStudent,
            ExamSessionStudentSaveRequest request) {

        // Map request answers to snapshot answers
        Map<Long, Long> questionIdToAnswerIdMap = request.getQuestions().stream()
                .collect(Collectors.toMap(QuestionSaveRequest::getQuestionId, QuestionSaveRequest::getAnswerId));

        examSessionStudent.getAnswers().forEach(studentAnswer -> {
            Long questionId = studentAnswer.getExamSessionQuestionSnapshot().getOriginalQuestionId();
            Long selectedAnswerId = questionIdToAnswerIdMap.get(questionId);

            if (selectedAnswerId != null) {
                // Find the corresponding snapshot answer
                ExamSessionAnswerSnapshot snapshotAnswer = studentAnswer.getExamSessionQuestionSnapshot()
                        .getExamSessionAnswerSnapshots().stream()
                        .filter(a -> a.getOriginalAnswerId().equals(selectedAnswerId))
                        .findFirst()
                        .orElse(null);
                studentAnswer.setSelectedAnswerSnapshot(snapshotAnswer);
            }
        });
        examSessionStudentRepository.save(examSessionStudent);
    }

    private void saveFinalExamSessionStudentAnswer(ExamSessionStudent examSessionStudent,
            ExamSessionStudentSaveRequest request) {
        // Map request answers to snapshot answers
        Map<Long, Long> questionIdToAnswerIdMap = request.getQuestions().stream()
                .collect(Collectors.toMap(QuestionSaveRequest::getQuestionId, QuestionSaveRequest::getAnswerId));

        examSessionStudent.setStatus(COMPLETED);
        examSessionStudent.setSubmittedAt(TimeUtils.getCurrentTime());

        examSessionStudent.getAnswers().forEach(studentAnswer -> {
            Long questionId = studentAnswer.getExamSessionQuestionSnapshot().getOriginalQuestionId();
            Long selectedAnswerId = questionIdToAnswerIdMap.get(questionId);

            if (selectedAnswerId != null) {
                // Find the corresponding snapshot answer
                ExamSessionAnswerSnapshot snapshotAnswer = studentAnswer.getExamSessionQuestionSnapshot()
                        .getExamSessionAnswerSnapshots().stream()
                        .filter(a -> a.getOriginalAnswerId().equals(selectedAnswerId))
                        .findFirst()
                        .orElse(null);
                studentAnswer.setSelectedAnswerSnapshot(snapshotAnswer);
            }
        });

        examSessionStudent.setTotalScore(computeScore(examSessionStudent));
        examSessionStudentRepository.save(examSessionStudent);
    }

    private Float computeScore(ExamSessionStudent entity) {
        if (entity.getAnswers() == null || entity.getAnswers().isEmpty()) {
            return 0f;
        }
        long correctCount = entity.getAnswers().stream()
                .filter(a -> a.getSelectedAnswerSnapshot() != null && a.getSelectedAnswerSnapshot().getIsCorrect())
                .count();
        int totalQuestions = entity.getAnswers().size();
        return (float) correctCount / totalQuestions * 10;
    }

    private Float computeFallbackScore(ExamSessionStudent entity) {
        return computeScore(entity);
    }

}