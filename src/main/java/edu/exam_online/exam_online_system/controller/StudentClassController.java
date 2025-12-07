package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.commons.PageResponse;
import edu.exam_online.exam_online_system.dto.response.classes.StudentClassDetailResponse;
import edu.exam_online.exam_online_system.dto.response.classes.StudentClassResponse;
import edu.exam_online.exam_online_system.service.classes.StudentClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/classes")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Student Class Management", description = "APIs for students to view their classes")
public class StudentClassController {

    StudentClassService studentClassService;

    @GetMapping
    @Operation(summary = "Get student's classes", description = "Get all classes that the student is enrolled in")
    public PageResponse<StudentClassResponse> getMyClasses(@ParameterObject Pageable pageable) {
        return PageResponse.success(studentClassService.getMyClasses(pageable));
    }

    @GetMapping("/{classId}")
    @Operation(summary = "Get class detail", description = "Get detailed information of a class including exam sessions")
    public BaseResponse<StudentClassDetailResponse> getClassDetail(@PathVariable Long classId) {
        return BaseResponse.success(studentClassService.getClassDetail(classId));
    }

    @PostMapping("/join")
    @Operation(summary = "Join class by code", description = "Student joins a class using class code")
    public BaseResponse<StudentClassResponse> joinClassByCode(@RequestParam String classCode) {
        return BaseResponse.success(studentClassService.joinClassByCode(classCode));
    }
}
