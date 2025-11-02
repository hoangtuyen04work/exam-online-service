package edu.exam_online.exam_online_system.repository.exam;

import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamSessionStudentRepository extends JpaRepository<ExamSessionStudent, Long> {
    ExamSessionStudent findByIdAndStudentId(Long id, Long studentId);

    ExamSessionStudent findByExamSessionIdAndStudentId(Long examSessionId, Long studentId);

    boolean existsByExamSessionIdAndStudentId(Long examSessionId, Long studentId);

}