package edu.exam_online.exam_online_system.service.classes.impl;

import edu.exam_online.exam_online_system.dto.response.classes.StudentClassDetailResponse;
import edu.exam_online.exam_online_system.dto.response.classes.StudentClassResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.classes.Class;
import edu.exam_online.exam_online_system.entity.classes.ClassStudent;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.classes.ClassRepository;
import edu.exam_online.exam_online_system.repository.classes.ClassStudentRepository;
import edu.exam_online.exam_online_system.service.classes.StudentClassService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentClassServiceImpl implements StudentClassService {

    ClassStudentRepository classStudentRepository;
    ClassRepository classRepository;
    UserRepository userRepository;

    static String WEB_DOMAIN = "http://localhost:3000/exam/";

    @Override
    @Transactional(readOnly = true)
    public Page<StudentClassResponse> getMyClasses(Pageable pageable) {
        log.info("Getting classes for student");

        Long studentId = SecurityUtils.getUserId();
        Page<ClassStudent> classStudents = classStudentRepository.findByStudentId(studentId, pageable);

        return classStudents.map(this::toStudentClassResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentClassDetailResponse getClassDetail(Long classId) {
        log.info("Getting class detail for classId: {}", classId);

        Long studentId = SecurityUtils.getUserId();

        // Verify student is enrolled in this class
        ClassStudent classStudent = classStudentRepository.findByClassIdAndStudentId(classId, studentId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        Class classEntity = classStudent.getClassEntity();

        var examSessions = classEntity.getClassExamSessions().stream()
                .map(ces -> StudentClassDetailResponse.ExamSessionInfo.builder()
                        .classExamSessionId(ces.getId())
                        .examSessionId(ces.getExamSession().getId())
                        .examSessionName(ces.getExamSession().getName())
                        .examSessionCode(ces.getExamSession().getCode())
                        .description(ces.getExamSession().getDescription())
                        .startAt(ces.getExamSession().getStartAt())
                        .expiredAt(ces.getExamSession().getExpiredAt())
                        .durationMinutes(ces.getExamSession().getDurationMinutes())
                        .assignedAt(ces.getAssignedAt())
                        .inviteLink(generateInviteExamSession(ces.getExamSession().getCode()))
                        .build())
                .collect(Collectors.toList());

        return StudentClassDetailResponse.builder()
                .classId(classEntity.getId())
                .classCode(classEntity.getClassCode())
                .name(classEntity.getName())
                .description(classEntity.getDescription())
                .semester(classEntity.getSemester())
                .academicYear(classEntity.getAcademicYear())
                .teacherName(classEntity.getTeacher().getUsername())
                .teacherEmail(classEntity.getTeacher().getEmail())
                .totalStudents(classEntity.getClassStudents().size())
                .examSessions(examSessions)
                .enrolledAt(classStudent.getEnrolledAt())
                .build();
    }

    private StudentClassResponse toStudentClassResponse(ClassStudent classStudent) {
        Class classEntity = classStudent.getClassEntity();
        return StudentClassResponse.builder()
                .classId(classEntity.getId())
                .classCode(classEntity.getClassCode())
                .name(classEntity.getName())
                .description(classEntity.getDescription())
                .semester(classEntity.getSemester())
                .academicYear(classEntity.getAcademicYear())
                .teacherName(classEntity.getTeacher().getFirstName() + " " + classEntity.getTeacher().getLastName())
                .totalStudents(classEntity.getClassStudents().size())
                .totalExamSessions(classEntity.getClassExamSessions().size())
                .enrolledAt(classStudent.getEnrolledAt())
                .build();
    }

    @Override
    @Transactional
    public StudentClassResponse joinClassByCode(String classCode) {
        log.info("Student joining class with code: {}", classCode);

        Long studentId = SecurityUtils.getUserId();
        User user = userRepository.findById(studentId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Find class by code
        Class classEntity = classRepository.findByClassCode(classCode)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        // Check if student is already enrolled
        if (classStudentRepository.existsByClassEntityIdAndStudentId(classEntity.getId(), studentId)) {
            throw new AppException(ErrorCode.STUDENT_ALREADY_IN_CLASS);
        }

        // Create new enrollment
        ClassStudent classStudent = ClassStudent.builder()
                .classEntity(classEntity)
                .student(user)
                .build();

        classStudentRepository.save(classStudent);

        return toStudentClassResponse(classStudent);
    }

    private String generateInviteExamSession(String code) {
        return WEB_DOMAIN + "join/" + code;
    }
}
