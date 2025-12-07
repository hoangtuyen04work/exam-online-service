package edu.exam_online.exam_online_system.mapper.classes;

import edu.exam_online.exam_online_system.dto.request.classes.ClassCreationRequest;
import edu.exam_online.exam_online_system.dto.request.classes.ClassUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.classes.ClassDetailResponse;
import edu.exam_online.exam_online_system.dto.response.classes.ClassResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.classes.Class;
import edu.exam_online.exam_online_system.entity.classes.ClassExamSession;
import edu.exam_online.exam_online_system.entity.classes.ClassStudent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teacher", source = "teacher")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "classStudents", ignore = true)
    @Mapping(target = "classExamSessions", ignore = true)
    Class toEntity(ClassCreationRequest request, User teacher);

    void updateEntity(@MappingTarget Class classEntity, ClassUpdateRequest request);

    default ClassResponse toClassResponse(Class classEntity) {
        return ClassResponse.builder()
                .classId(classEntity.getId())
                .classCode(classEntity.getClassCode())
                .name(classEntity.getName())
                .description(classEntity.getDescription())
                .semester(classEntity.getSemester())
                .academicYear(classEntity.getAcademicYear())
                .isActive(classEntity.getIsActive())
                .teacherId(classEntity.getTeacher().getId())
                .teacherName(classEntity.getTeacher().getFirstName() + " " + classEntity.getTeacher().getLastName())
                .totalStudents(classEntity.getClassStudents().size())
                .totalExamSessions(classEntity.getClassExamSessions().size())
                .createdAt(classEntity.getCreatedAt())
                .updatedAt(classEntity.getUpdatedAt())
                .build();
    }

    default ClassDetailResponse toClassDetailResponse(Class classEntity) {
        User teacher = classEntity.getTeacher();

        List<ClassDetailResponse.StudentInClassResponse> students = classEntity.getClassStudents().stream()
                .map(this::toStudentInClassResponse)
                .collect(Collectors.toList());

        List<ClassDetailResponse.ExamSessionInClassResponse> examSessions = classEntity.getClassExamSessions().stream()
                .map(this::toExamSessionInClassResponse)
                .collect(Collectors.toList());

        return ClassDetailResponse.builder()
                .id(classEntity.getId())
                .classCode(classEntity.getClassCode())
                .name(classEntity.getName())
                .description(classEntity.getDescription())
                .semester(classEntity.getSemester())
                .academicYear(classEntity.getAcademicYear())
                .isActive(classEntity.getIsActive())
                .teacherId(teacher.getId())
                .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                .teacherEmail(teacher.getEmail())
                .students(students)
                .examSessions(examSessions)
                .createdAt(classEntity.getCreatedAt())
                .updatedAt(classEntity.getUpdatedAt())
                .build();
    }

    default ClassDetailResponse.StudentInClassResponse toStudentInClassResponse(ClassStudent classStudent) {
        User student = classStudent.getStudent();
        return ClassDetailResponse.StudentInClassResponse.builder()
                .id(classStudent.getId())
                .studentId(student.getId())
                .username(student.getUsername())
                .email(student.getEmail())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .isActive(classStudent.getIsActive())
                .enrolledAt(classStudent.getEnrolledAt())
                .build();
    }

    default ClassDetailResponse.ExamSessionInClassResponse toExamSessionInClassResponse(
            ClassExamSession classExamSession) {
        return ClassDetailResponse.ExamSessionInClassResponse.builder()
                .id(classExamSession.getId())
                .examSessionId(classExamSession.getExamSession().getId())
                .examSessionName(classExamSession.getExamSession().getName())
                .examSessionCode(classExamSession.getExamSession().getCode())
                .description(classExamSession.getExamSession().getDescription())
                .startAt(classExamSession.getExamSession().getStartAt())
                .expiredAt(classExamSession.getExamSession().getExpiredAt())
                .durationMinutes(classExamSession.getExamSession().getDurationMinutes())
                .assignedAt(classExamSession.getAssignedAt())
                .build();
    }
}
