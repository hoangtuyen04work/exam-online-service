package edu.exam_online.exam_online_system.service.exam;

import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BankQuestionService {
    void deleteById(Long bankQuestionId);

    BankQuestionDetailResponse getById(Long bankQuestionId);

    Page<BankQuestionResponse> searchBankQuestion(Pageable pageable);

    void createBankQuestion(BankQuestionCreationRequest request);
}
