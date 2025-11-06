package edu.exam_online.exam_online_system.service.exam;

import edu.exam_online.exam_online_system.commons.constant.ExamSubmitStateEnum;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionStudentSaveRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExitEventRequest;
import edu.exam_online.exam_online_system.dto.request.exam.JoinExamRequest;
import edu.exam_online.exam_online_system.dto.request.exam.TeacherOverallFeedBackRequest;
import edu.exam_online.exam_online_system.dto.response.exam.student.ExamSessionContentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.ExamSessionStudentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.JoinExamSessionResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.result.ExamSessionStudentResultResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.StudentJoinedExamSessionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamSessionStudentService {
    void teacherFeedBack(Long examSessionStudentId, TeacherOverallFeedBackRequest request);

    Page<StudentJoinedExamSessionResponse> getStudentJoinedExamSession(Long examSessionId, Pageable pageable);

    ExamSessionStudentResultResponse getResultByExamSessionId(Long examSessionId);

    ExamSessionStudentResultResponse getExamSessionResultByExamSessionStudentId(Long examSessionStudentId);

    void autoSaveExamSessionStudent();

    Page<ExamSessionStudentResponse> searchExamSession(Pageable pageable);

    void saveExitEvent(ExitEventRequest request);

    boolean saveExam(ExamSubmitStateEnum state, ExamSessionStudentSaveRequest request);

    ExamSessionContentResponse doExam(Long examSessionId);

    JoinExamSessionResponse joinExam(JoinExamRequest request);
}
