package edu.exam_online.exam_online_system.service.monitoring.impl;


import edu.exam_online.exam_online_system.dto.request.websocket.StudentEvent;
import edu.exam_online.exam_online_system.dto.request.websocket.StudentStatusResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudent;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.ExamSessionStudentMapper;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamSessionRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamSessionStudentRepository;
import edu.exam_online.exam_online_system.service.monitoring.StudentMonitoringService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class StudentMonitoringServiceImpl implements StudentMonitoringService {

    ExamSessionRepository examSessionRepository;
    ExamSessionStudentRepository examSessionStudentRepository;
    UserRepository userRepository;

    ExamSessionStudentMapper examSessionStudentMapper;

    @Override
    public List<StudentStatusResponse> getStudentParticipant(Long examSessionId){
        List<ExamSessionStudent> examSessionStudents = examSessionStudentRepository.findByExamSessionId(examSessionId);

        return examSessionStudents.stream().map(examSessionStudentMapper::toStudentStatusResponse)
                .toList();
    }

    @Override
    public User handleStudentEvent(Principal principal, StudentEvent event, String sessionId){
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (event.getExamSessionId() == null) {
            throw new AppException(ErrorCode.EXAM_SESSION_NOT_FOUND);
        }

        if (!examSessionStudentRepository.existsByExamSessionIdAndStudentId(event.getExamSessionId(), userId)) {
            throw new AppException(ErrorCode.CAN_NOT_SEND_MESSAGE);
        }

        return user;
    }
}
