package edu.exam_online.exam_online_system.service.exam;

import edu.exam_online.exam_online_system.commons.constant.ExamSubmitStateEnum;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionStudentSaveRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExitEventRequest;
import edu.exam_online.exam_online_system.dto.request.exam.JoinExamRequest;
import edu.exam_online.exam_online_system.dto.response.exam.student.ExamSessionContentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.JoinExamSessionResponse;

public interface ExamSessionStudentService {
    void saveExitEvent(ExitEventRequest request);

    boolean saveExam(ExamSubmitStateEnum state, ExamSessionStudentSaveRequest request);

    ExamSessionContentResponse doExam(Long examSessionId);

    JoinExamSessionResponse joinExam(JoinExamRequest request);
}
