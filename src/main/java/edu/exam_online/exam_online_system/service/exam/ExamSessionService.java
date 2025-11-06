package edu.exam_online.exam_online_system.service.exam;

import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionUpdateRequest;
import edu.exam_online.exam_online_system.dto.request.param.ExamSessionSearchParam;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamSessionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamSessionService {
    ExamSessionResponse createSession(ExamSessionCreationRequest request);

    ExamSessionResponse updateSession(Long examSessionId, ExamSessionUpdateRequest request);

    void deleteSession(Long examSessionId);

    Page<ExamSessionResponse> getAll(ExamSessionSearchParam param, Pageable pageable);

    ExamSessionResponse getDetail(Long examSessionId);
}
