package edu.exam_online.exam_online_system.service.exam;

import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionSearchParam;
import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionUpdateRequest;
import edu.exam_online.exam_online_system.dto.request.exam.QuestionUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BankQuestionService {
    void deleteById(Long bankQuestionId);

    BankQuestionDetailResponse getById(Long bankQuestionId);

    Page<BankQuestionResponse> searchBankQuestion(BankQuestionSearchParam param, Pageable pageable);

    void createBankQuestion(BankQuestionCreationRequest request);

    void updateBankQuestion(Long bankQuestionId, BankQuestionUpdateRequest request);

    void updateQuestions(Long bankQuestionId, List<QuestionUpdateRequest> questions);
}
