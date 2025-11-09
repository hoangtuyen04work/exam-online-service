package edu.exam_online.exam_online_system.service.exam.impl;

import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionUpdateRequest;
import edu.exam_online.exam_online_system.dto.request.param.ExamSessionSearchParam;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamSessionResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.Exam;
import edu.exam_online.exam_online_system.entity.exam.ExamSession;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.ExamSessionMapper;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamSessionRepository;
import edu.exam_online.exam_online_system.service.exam.ExamSessionService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ExamSessionServiceImpl implements ExamSessionService {
    ExamSessionRepository examSessionRepository;
    UserRepository userRepository;
    ExamRepository examRepository;

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
        return examSessionMapper.toResponse(session, inviteUrl);
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
        Page<ExamSession> examSessions =  examSessionRepository.findAllByOwnerIdOrderByCreatedAtDesc(param.getExamId(), userId, pageable);
        return examSessions.map(
                examSession -> examSessionMapper.toResponse(examSession, generateInviteExamSession(examSession.getCode())));
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

    private String generateInviteExamSession(String code){
        return WEB_DOMAIN +"join/" + code;
    }
}
