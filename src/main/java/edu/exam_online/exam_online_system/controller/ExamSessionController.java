package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.commons.PageResponse;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionUpdateRequest;
import edu.exam_online.exam_online_system.dto.request.exam.TeacherOverallFeedBackRequest;
import edu.exam_online.exam_online_system.dto.request.param.ExamSessionSearchParam;
import edu.exam_online.exam_online_system.dto.request.websocket.StudentStatusResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.result.ExamSessionStudentResultResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamSessionResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamSessionStatisticsResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.StudentJoinedExamSessionResponse;
import edu.exam_online.exam_online_system.service.exam.ExamSessionService;
import edu.exam_online.exam_online_system.service.exam.ExamSessionStudentService;
import edu.exam_online.exam_online_system.service.monitoring.StudentMonitoringService;
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

import java.util.List;

@RestController
@RequestMapping("/api/teacher/exam-sessions")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExamSessionController {

    ExamSessionService examSessionService;
    ExamSessionStudentService  examSessionStudentService;
    StudentMonitoringService monitoringService;

    @GetMapping("/monitoring/{examSessionId}")
    @Operation(summary ="Get all exam session of a exam")
    public BaseResponse<List<StudentStatusResponse>> getStudentParticipant(@PathVariable Long examSessionId){
        return BaseResponse.success(monitoringService.getStudentParticipant(examSessionId));
    }

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
    @Operation(summary ="Get all exam session of a exam")
    public PageResponse<ExamSessionResponse> getAll(@ParameterObject ExamSessionSearchParam param, @ParameterObject Pageable pageable){
        return PageResponse.success(examSessionService.getAll(param, pageable));
    }

    @GetMapping
    @Operation(summary ="Get detail exam session")
    public BaseResponse<ExamSessionResponse> getExamSession(@RequestParam Long examSessionId){
        return BaseResponse.success(examSessionService.getDetail(examSessionId));
    }

    @GetMapping("/{examSessionId}")
    @Operation(summary = "Get list user of exam session")
    public PageResponse<StudentJoinedExamSessionResponse> getStudentJoinedExamSession(@PathVariable Long examSessionId, @ParameterObject Pageable pageable){
        return PageResponse.success(examSessionStudentService.getStudentJoinedExamSession(examSessionId, pageable));
    }

    @GetMapping("/result/{examSessionStudentId}")
    @Operation(summary = "Get result of exam of a student")
    public BaseResponse<ExamSessionStudentResultResponse> getExamSessionResultById(@PathVariable Long examSessionStudentId) {
        return BaseResponse.success(examSessionStudentService.getExamSessionResultByExamSessionStudentId(examSessionStudentId));
    }

    @PostMapping("/{examSessionStudentId}")
    @Operation(summary = "Teacher create feed back for student")
    public BaseResponse<Void> createFeedBack(@PathVariable Long examSessionStudentId, @RequestBody TeacherOverallFeedBackRequest request){
        examSessionStudentService.teacherFeedBack(examSessionStudentId, request);
        return BaseResponse.success();
    }

    @PutMapping("/{examSessionId}/passing-score")
    @Operation(summary = "Update passing score for exam session")
    public BaseResponse<ExamSessionResponse> updatePassingScore(
            @PathVariable Long examSessionId, 
            @RequestBody edu.exam_online.exam_online_system.dto.request.exam.UpdatePassingScoreRequest request) {
        return BaseResponse.success(examSessionService.updatePassingScore(examSessionId, request.getPassingScore()));
    }

    @GetMapping("/{examSessionId}/statistics")
    @Operation(summary = "Get statistics for exam session")
    public BaseResponse<ExamSessionStatisticsResponse> getExamSessionStatistics(@PathVariable Long examSessionId) {
        return BaseResponse.success(examSessionService.getExamSessionStatistics(examSessionId));
    }
}
