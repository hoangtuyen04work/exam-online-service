package edu.exam_online.exam_online_system.service.exam;

import edu.exam_online.exam_online_system.dto.request.exam.ExamCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamUpdateQuestionsRequest;
import edu.exam_online.exam_online_system.dto.response.exam.ExamDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.ExamResponse;
import edu.exam_online.exam_online_system.dto.request.exam.ExamUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamService {
    void updateExam(Long examId, ExamUpdateQuestionsRequest request);

    Page<ExamResponse> getAllExam(Pageable pageable);

    void updateExamQuestions(Long examId, ExamUpdateQuestionsRequest request);

    ExamDetailResponse getExamDetail(Long examId);

    ExamDetailResponse updateExam(Long examId, ExamUpdateRequest request);

    ExamResponse getExamById(Long id);

    ExamResponse createExam(ExamCreationRequest request);
}
