package edu.exam_online.exam_online_system.service.monitoring.impl;


import edu.exam_online.exam_online_system.dto.request.websocket.StudentEvent;
import edu.exam_online.exam_online_system.dto.request.websocket.StudentStatusResponse;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudent;
import edu.exam_online.exam_online_system.mapper.ExamSessionStudentMapper;
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

    ExamSessionStudentMapper examSessionStudentMapper;

    @Override
    public List<StudentStatusResponse> getStudentParticipant(Long examSessionId){
        List<ExamSessionStudent> examSessionStudents = examSessionStudentRepository.findByExamSessionId(examSessionId);

        return examSessionStudents.stream().map(examSessionStudentMapper::toStudentStatusResponse)
                .toList();
    }

    @Override
    public void handleStudentEvent(Principal principal, StudentEvent event, String sessionId){
//        String username = principal.getName();  // username từ JWT hoặc session
//
//        // 1) Validate input
//        if (event.getExamId() == null) {
//            return; // hoặc throw error
//        }
//
//        // 2) Kiểm tra xem user có quyền gửi event cho exam đó không
//        // (tùy hệ thống bạn implement)
//        if (!examMonitorService.isStudentInExam(username, event.getExamId())) {
//            System.out.println("INVALID ACCESS from user: " + username);
//            return;
//        }
//
//        // 3) Lưu event (để sau xem lại hành vi học sinh)
//        examMonitorService.saveEvent(username, event.getExamId(), event.getType(), event.getPayload());

    }
}
