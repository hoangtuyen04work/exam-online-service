package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.commons.PageResponse;
import edu.exam_online.exam_online_system.dto.request.exam.ExamCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamUpdateQuestionsRequest;
import edu.exam_online.exam_online_system.dto.response.exam.ExamDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.ExamResponse;
import edu.exam_online.exam_online_system.dto.request.exam.ExamUpdateRequest;
import edu.exam_online.exam_online_system.service.exam.ExamService;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher/exams")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExamController {

    ExamService examService;

    @PutMapping("/bulk-update/{examId}")
    @Description("Update all info of exam like basic, question and answer")
    public BaseResponse<Void> bulkUpdate(@PathVariable Long examId, @RequestBody ExamUpdateQuestionsRequest request){
        examService.updateExam(examId, request);
        return BaseResponse.success();
    }

    @PostMapping
    @Description("Create new exam")
    public BaseResponse<ExamResponse> createExam(@RequestBody ExamCreationRequest request){
        return BaseResponse.success(examService.createExam(request));
    }

    @GetMapping("/basic/{examId}")
    @Description("Get basic in for of exam by id")
    public BaseResponse<ExamResponse> getExam(@PathVariable Long examId){
        return BaseResponse.success(examService.getExamById(examId));
    }

    @GetMapping
    @Description("Get all exam")
    public PageResponse<ExamResponse> getAllExam(@ParameterObject Pageable pageable){
        return PageResponse.success(examService.getAllExam(pageable));
    }

    @GetMapping("/{examId}")
    @Description("Get exam detail by id")
    public BaseResponse<ExamDetailResponse> getExamDetail(
            @PathVariable Long examId) {
        return BaseResponse.success(examService.getExamDetail(examId));
    }

    @PutMapping("/{examId}")
    @Description("Update exam by id")
    public BaseResponse<ExamDetailResponse> updateExam(
            @PathVariable Long examId,
            @RequestBody ExamUpdateRequest request) {
        return BaseResponse.success(examService.updateExam(examId, request));
    }

    @PutMapping("/{examId}/questions")
    @Description("Update exam questions by id")
    public BaseResponse<String> updateExamQuestions(
            @PathVariable Long examId,
            @RequestBody ExamUpdateQuestionsRequest request) {
        examService.updateExamQuestions(examId, request);
        return BaseResponse.success("Exam questions updated successfully");
    }
}
