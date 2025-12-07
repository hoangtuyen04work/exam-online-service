package edu.exam_online.exam_online_system.service.classes;

import edu.exam_online.exam_online_system.dto.request.classes.AddExamSessionsToClassRequest;
import edu.exam_online.exam_online_system.dto.request.classes.AddStudentsToClassRequest;
import edu.exam_online.exam_online_system.dto.request.classes.ClassCreationRequest;
import edu.exam_online.exam_online_system.dto.request.classes.ClassUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.classes.ClassDetailResponse;
import edu.exam_online.exam_online_system.dto.response.classes.ClassResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClassService {

    ClassResponse createClass(ClassCreationRequest request);

    ClassResponse updateClass(Long classId, ClassUpdateRequest request);

    void deleteClass(Long classId);

    ClassResponse getClassById(Long classId);

    ClassDetailResponse getClassDetail(Long classId);

    Page<ClassResponse> getAllClasses(Pageable pageable);

    void addStudentsToClass(Long classId, AddStudentsToClassRequest request);

    void removeStudentFromClass(Long classId, Long studentId);

    void addExamSessionsToClass(Long classId, AddExamSessionsToClassRequest request);

    void removeExamSessionFromClass(Long classId, Long classExamSessionId);
}
