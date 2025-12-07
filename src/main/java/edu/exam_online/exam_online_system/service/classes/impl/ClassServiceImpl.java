package edu.exam_online.exam_online_system.service.classes.impl;

import edu.exam_online.exam_online_system.commons.constant.RoleEnum;
import edu.exam_online.exam_online_system.dto.request.classes.AddExamSessionsToClassRequest;
import edu.exam_online.exam_online_system.dto.request.classes.AddStudentsToClassRequest;
import edu.exam_online.exam_online_system.dto.request.classes.ClassCreationRequest;
import edu.exam_online.exam_online_system.dto.request.classes.ClassUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.classes.ClassDetailResponse;
import edu.exam_online.exam_online_system.dto.response.classes.ClassResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.classes.Class;
import edu.exam_online.exam_online_system.entity.classes.ClassExamSession;
import edu.exam_online.exam_online_system.entity.classes.ClassStudent;
import edu.exam_online.exam_online_system.entity.exam.ExamSession;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.classes.ClassMapper;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.classes.ClassExamSessionRepository;
import edu.exam_online.exam_online_system.repository.classes.ClassRepository;
import edu.exam_online.exam_online_system.repository.classes.ClassStudentRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamSessionRepository;
import edu.exam_online.exam_online_system.service.auth.UserService;
import edu.exam_online.exam_online_system.service.classes.ClassService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassServiceImpl implements ClassService {

    ClassRepository classRepository;
    ClassStudentRepository classStudentRepository;
    ClassExamSessionRepository classExamSessionRepository;
    ExamSessionRepository examSessionRepository;
    UserRepository userRepository;

    ClassMapper classMapper;

    @Override
    @Transactional
    public ClassResponse createClass(ClassCreationRequest request) {
        log.info("Creating new class with name: {}", request.getName());

        String classCode = generateUniqueClassCode();
        log.info("Generated class code: {}", classCode);

        Long teacherId = SecurityUtils.getUserId();
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Class classEntity = classMapper.toEntity(request, teacher);
        classEntity.setClassCode(classCode);
        classEntity.setIsActive(true);

        Class savedClass = classRepository.save(classEntity);

        log.info("Class created successfully with ID: {}", savedClass.getId());
        return classMapper.toClassResponse(savedClass);
    }

    @Override
    @Transactional
    public ClassResponse updateClass(Long classId, ClassUpdateRequest request) {
        log.info("Updating class with ID: {}", classId);

        // Get class and verify ownership
        Class classEntity = getClassByIdAndVerifyOwnership(classId);

        classMapper.updateEntity(classEntity, request);

        Class updatedClass = classRepository.save(classEntity);

        log.info("Class updated successfully with ID: {}", updatedClass.getId());
        return classMapper.toClassResponse(updatedClass);
    }

    @Override
    @Transactional
    public void deleteClass(Long classId) {
        log.info("Deleting class with ID: {}", classId);

        Class classEntity = getClassByIdAndVerifyOwnership(classId);

        classRepository.delete(classEntity);

        log.info("Class deleted successfully with ID: {}", classId);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassResponse getClassById(Long classId) {
        log.info("Getting class by ID: {}", classId);

        Class classEntity = getClassByIdAndVerifyOwnership(classId);
        return classMapper.toClassResponse(classEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassDetailResponse getClassDetail(Long classId) {
        log.info("Getting class detail by ID: {}", classId);

        Class classEntity = getClassByIdAndVerifyOwnership(classId);
        return classMapper.toClassDetailResponse(classEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassResponse> getAllClasses(Pageable pageable) {
        log.info("Getting all classes for current teacher");

        Long teacherId = SecurityUtils.getUserId();
        Page<Class> classes = classRepository.findByTeacherId(teacherId, pageable);

        return classes.map(classMapper::toClassResponse);
    }

    @Override
    @Transactional
    public void addStudentsToClass(Long classId, AddStudentsToClassRequest request) {
        log.info("Adding {} students to class ID: {}", request.getStudentEmails().size(), classId);

        Class classEntity = getClassByIdAndVerifyOwnership(classId);

        List<User> users = userRepository.findAllByEmailInAndRoleName(request.getStudentEmails(),
                RoleEnum.STUDENT.name());
        List<ClassStudent> classStudents = new ArrayList<>();
        for (User user : users) {
            ClassStudent classStudent = ClassStudent.builder()
                    .classEntity(classEntity)
                    .student(user)
                    .isActive(true)
                    .build();
            classStudents.add(classStudent);
        }
        classStudentRepository.saveAll(classStudents);

        log.info("Students added successfully to class ID: {}", classId);
    }

    @Override
    @Transactional
    public void removeStudentFromClass(Long classId, Long studentId) {
        log.info("Removing student ID {} from class ID {}", studentId, classId);

        getClassByIdAndVerifyOwnership(classId);

        if (!classStudentRepository.existsByClassEntityIdAndStudentId(classId, studentId)) {
            throw new AppException(ErrorCode.STUDENT_NOT_IN_CLASS);
        }

        classStudentRepository.deleteByClassIdAndStudentId(classId, studentId);

        log.info("Student removed successfully from class ID: {}", classId);
    }

    @Override
    @Transactional
    public void addExamSessionsToClass(Long classId, AddExamSessionsToClassRequest request) {
        log.info("Adding {} exam sessions to class ID: {}", request.getExamSessionIds().size(), classId);

        Class classEntity = getClassByIdAndVerifyOwnership(classId);
        Long teacherId = SecurityUtils.getUserId();

        List<ExamSession> examSessions = examSessionRepository.findAllByIdInAndOwnerId(request.getExamSessionIds(),
                teacherId);
        if (request.getExamSessionIds().size() != examSessions.size()) {
            throw new AppException(ErrorCode.EXAM_SESSION_NOT_FOUND);
        }

        List<ClassExamSession> classExamSessions = new ArrayList<>();

        examSessions.forEach(examSession -> {
            ClassExamSession classExamSession = ClassExamSession.builder()
                    .classEntity(classEntity)
                    .examSession(examSession)
                    .build();
            classExamSessions.add(classExamSession);
        });
        classExamSessionRepository.saveAll(classExamSessions);

        log.info("Exam sessions added successfully to class ID: {}", classId);
    }

    @Override
    @Transactional
    public void removeExamSessionFromClass(Long classId, Long classExamSessionId) {
        log.info("Removing class exam session ID {} from class ID {}", classExamSessionId, classId);

        getClassByIdAndVerifyOwnership(classId);

        ClassExamSession classExamSession = classExamSessionRepository.findById(classExamSessionId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SESSION_NOT_IN_CLASS));

        if (!classExamSession.getClassEntity().getId().equals(classId)) {
            throw new AppException(ErrorCode.EXAM_SESSION_NOT_IN_CLASS);
        }

        classExamSessionRepository.delete(classExamSession);

        log.info("Class exam session removed successfully from class ID: {}", classId);
    }

    private Class getClassByIdAndVerifyOwnership(Long classId) {
        Long teacherId = SecurityUtils.getUserId();
        return classRepository.findByIdAndTeacherId(classId, teacherId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
    }

    private String generateUniqueClassCode() {
        String classCode = "";
        for (int i = 0; i < 10; i++) {
            String year = String.valueOf(Year.now().getValue());
            String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
            classCode = "CLS-" + year + randomPart;
        }
        return classCode;
    }
}
