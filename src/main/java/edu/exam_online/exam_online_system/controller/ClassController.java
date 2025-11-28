package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.commons.PageResponse;
import edu.exam_online.exam_online_system.dto.request.classes.AddExamSessionsToClassRequest;
import edu.exam_online.exam_online_system.dto.request.classes.AddStudentsToClassRequest;
import edu.exam_online.exam_online_system.dto.request.classes.ClassCreationRequest;
import edu.exam_online.exam_online_system.dto.request.classes.ClassUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.classes.ClassDetailResponse;
import edu.exam_online.exam_online_system.dto.response.classes.ClassResponse;
import edu.exam_online.exam_online_system.service.classes.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher/classes")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Class Management", description = "APIs for teacher to manage classes")
public class ClassController {

    ClassService classService;

    @PostMapping
    @Operation(summary = "Create new class", description = "Teacher creates a new class")
    public BaseResponse<ClassResponse> createClass(@Valid @RequestBody ClassCreationRequest request) {
        return BaseResponse.success(classService.createClass(request));
    }

    @PutMapping("/{classId}")
    @Operation(summary = "Update class", description = "Teacher updates class information")
    public BaseResponse<ClassResponse> updateClass(
            @PathVariable Long classId,
            @Valid @RequestBody ClassUpdateRequest request) {
        return BaseResponse.success(classService.updateClass(classId, request));
    }

    @DeleteMapping("/{classId}")
    @Operation(summary = "Delete class", description = "Teacher deletes a class")
    public BaseResponse<Void> deleteClass(@PathVariable Long classId) {
        classService.deleteClass(classId);
        return BaseResponse.success();
    }

    @GetMapping("/{classId}")
    @Operation(summary = "Get class basic info", description = "Get basic information of a class")
    public BaseResponse<ClassResponse> getClassById(@PathVariable Long classId) {
        return BaseResponse.success(classService.getClassById(classId));
    }

    @GetMapping("/{classId}/detail")
    @Operation(summary = "Get class detail", description = "Get detailed information of a class including students and exam sessions")
    public BaseResponse<ClassDetailResponse> getClassDetail(@PathVariable Long classId) {
        return BaseResponse.success(classService.getClassDetail(classId));
    }

    @GetMapping
    @Operation(summary = "Get all classes", description = "Get all classes of current teacher with pagination")
    public PageResponse<ClassResponse> getAllClasses(@ParameterObject Pageable pageable) {
        return PageResponse.success(classService.getAllClasses(pageable));
    }

    @PostMapping("/{classId}/students")
    @Operation(summary = "Add students to class", description = "Teacher adds multiple students to a class")
    public BaseResponse<Void> addStudentsToClass(
            @PathVariable Long classId,
            @Valid @RequestBody AddStudentsToClassRequest request) {
        classService.addStudentsToClass(classId, request);
        return BaseResponse.success();
    }

    @DeleteMapping("/{classId}/students/{studentId}")
    @Operation(summary = "Remove student from class", description = "Teacher removes a student from a class")
    public BaseResponse<Void> removeStudentFromClass(
            @PathVariable Long classId,
            @PathVariable Long studentId) {
        classService.removeStudentFromClass(classId, studentId);
        return BaseResponse.success();
    }

    @PostMapping("/{classId}/exam-sessions")
    @Operation(summary = "Add exam sessions to class", description = "Teacher assigns multiple exam sessions to a class")
    public BaseResponse<Void> addExamSessionsToClass(
            @PathVariable Long classId,
            @Valid @RequestBody AddExamSessionsToClassRequest request) {
        classService.addExamSessionsToClass(classId, request);
        return BaseResponse.success();
    }

    @DeleteMapping("/{classId}/exam-sessions/{classExamSessionId}")
    @Operation(summary = "Remove exam session from class", description = "Teacher removes a specific class exam session assignment")
    public BaseResponse<Void> removeExamSessionFromClass(
            @PathVariable Long classId,
            @PathVariable Long classExamSessionId) {
        classService.removeExamSessionFromClass(classId, classExamSessionId);
        return BaseResponse.success();
    }
}
