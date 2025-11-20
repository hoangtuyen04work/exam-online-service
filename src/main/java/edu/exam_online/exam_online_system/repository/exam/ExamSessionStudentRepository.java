package edu.exam_online.exam_online_system.repository.exam;

import edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSessionStudentRepository extends JpaRepository<ExamSessionStudent, Long> {

    List<ExamSessionStudent> findByExamSessionId(Long examSessionId);

    ExamSessionStudent findByIdAndStudentId(Long id, Long studentId);

    ExamSessionStudent findByExamSessionIdAndStudentId(Long examSessionId, Long studentId);

    boolean existsByExamSessionIdAndStudentId(Long examSessionId, Long studentId);

    Page<ExamSessionStudent> findByStudentIdAndStatusOrderByCreatedAtDesc(Long studentId, ExamStudentStatusEnum status, Pageable pageable);

    @Query("SELECT es FROM ExamSessionStudent es WHERE es.examSession.expiredAt < CURRENT_TIMESTAMP")
    List<ExamSessionStudent> findExpiredExamSessionStudent();

    Page<ExamSessionStudent> findByExamSessionIdOrderByCreatedAtDesc(Long examSessionId, Pageable pageable);
}