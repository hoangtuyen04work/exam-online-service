package edu.exam_online.exam_online_system.controller;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.commons.PageResponse;
import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionSearchParam;
import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionResponse;
import edu.exam_online.exam_online_system.service.exam.BankQuestionService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank-questions")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BankQuestionController {
    BankQuestionService bankQuestionService;

    @PostMapping
    @Operation(summary = "Create a new bank question (with question list)")
    public BaseResponse<Void> createBankQuestion(@RequestBody BankQuestionCreationRequest request) {
        bankQuestionService.createBankQuestion(request);
        return BaseResponse.success();
    }

    @GetMapping("/{bankQuestionId}")
    @Operation(summary = "Get bank question detail by ID")
    public BaseResponse<BankQuestionDetailResponse> getBankQuestionById(@PathVariable Long bankQuestionId) {
        return BaseResponse.success(bankQuestionService.getById(bankQuestionId));
    }

    @GetMapping
    @Operation(summary = "Search or list all bank questions of the current teacher")
    public PageResponse<BankQuestionResponse> searchBankQuestions(@ParameterObject BankQuestionSearchParam param,
            @ParameterObject Pageable pageable) {
        return PageResponse.success(bankQuestionService.searchBankQuestion(param, pageable));
    }

    @PutMapping("/{bankQuestionId}")
    @Operation(summary = "Update a bank question by ID")
    public BaseResponse<Void> updateBankQuestion(
            @PathVariable Long bankQuestionId,
            @RequestBody BankQuestionUpdateRequest request) {
        bankQuestionService.updateBankQuestion(bankQuestionId, request);
        return BaseResponse.success();
    }

    @DeleteMapping("/{bankQuestionId}")
    @Operation(summary = "Delete a bank question by ID")
    public BaseResponse<Void> deleteBankQuestion(@PathVariable Long bankQuestionId) {
        bankQuestionService.deleteById(bankQuestionId);
        return BaseResponse.success();
    }
}
