package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.commons.constant.ExamSubmitStateEnum;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionStudentSaveRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExitEventRequest;
import edu.exam_online.exam_online_system.dto.request.exam.JoinExamRequest;
import edu.exam_online.exam_online_system.dto.response.exam.student.ExamSessionContentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.JoinExamSessionResponse;
import edu.exam_online.exam_online_system.service.exam.ExamSessionStudentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student/exam")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExamSessionStudentController {
    ExamSessionStudentService examSessionStudentService;


    @PostMapping("/exit")
    @Operation(summary = "Save exit event")
    public BaseResponse<Void> saveExitEvent(@RequestBody ExitEventRequest request) {
        examSessionStudentService.saveExitEvent(request);
        return BaseResponse.success();
    }

    @PostMapping("/{examSessionId}/do")
    @Operation(summary = "Api to do exam")
    public BaseResponse<ExamSessionContentResponse> doExam(@PathVariable Long examSessionId) {
        return BaseResponse.success(examSessionStudentService.doExam(examSessionId));
    }

    @PostMapping("/submit")
    @Operation(summary = "Api to save exam, if save temporary state is DRAFT, if user click save state is FINAL")
    public BaseResponse<Boolean> saveExam(
            @RequestParam ExamSubmitStateEnum state,
            @RequestBody ExamSessionStudentSaveRequest request) {
        return BaseResponse.success(examSessionStudentService.saveExam(state, request));
    }

    @PostMapping("/join")
    @Operation(summary = "Api to join exam session")
    public BaseResponse<JoinExamSessionResponse> joinExam(@RequestBody JoinExamRequest request) {
        return BaseResponse.success(examSessionStudentService.joinExam(request));
    }
}
