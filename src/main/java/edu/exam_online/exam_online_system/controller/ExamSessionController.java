package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.commons.PageResponse;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamSessionResponse;
import edu.exam_online.exam_online_system.service.exam.ExamSessionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher/exam-sessions")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExamSessionController {

    ExamSessionService examSessionService;

    @PostMapping
    @Operation(summary ="Create new exam session")
    public BaseResponse<ExamSessionResponse> createExamSession(@RequestBody ExamSessionCreationRequest request){
        return BaseResponse.success(examSessionService.createSession(request));
    }

    @PutMapping("/{examSessionId}")
    @Operation(summary ="Update exam session")
    public BaseResponse<ExamSessionResponse> updateExamSession(@PathVariable Long examSessionId, @RequestBody ExamSessionUpdateRequest request){
        return BaseResponse.success(examSessionService.updateSession(examSessionId, request));
    }

    @DeleteMapping("/{examSessionId}")
    @Operation(summary ="Delete exam session")
    public BaseResponse<Void> deleteExamSession(@PathVariable Long examSessionId){
        examSessionService.deleteSession(examSessionId);
        return BaseResponse.success();
    }

    @GetMapping("/search")
    @Operation(summary ="Get all exam session")
    public PageResponse<ExamSessionResponse> getAll(@ParameterObject Pageable pageable){
        return PageResponse.success(examSessionService.getAll(pageable));
    }

    @GetMapping
    @Operation(summary ="Get detail exam session")
    public BaseResponse<ExamSessionResponse> getExamSession(@RequestParam Long examSessionId){
        return BaseResponse.success(examSessionService.getDetail(examSessionId));
    }

}
