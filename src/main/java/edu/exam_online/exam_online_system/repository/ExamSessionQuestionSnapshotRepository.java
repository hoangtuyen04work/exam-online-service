package edu.exam_online.exam_online_system.repository;

import edu.exam_online.exam_online_system.entity.exam.ExamSessionQuestionSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSessionQuestionSnapshotRepository extends JpaRepository<ExamSessionQuestionSnapshot, Long> {

    List<ExamSessionQuestionSnapshot> findByExamSessionIdOrderByQuestionOrderAsc(Long examSessionId);

    @Query("SELECT esq FROM ExamSessionQuestionSnapshot esq " +
            "LEFT JOIN FETCH esq.examSessionAnswerSnapshots " +
            "WHERE esq.examSession.id = :examSessionId " +
            "ORDER BY esq.questionOrder ASC")
    List<ExamSessionQuestionSnapshot> findByExamSessionIdWithAnswers(@Param("examSessionId") Long examSessionId);

    void deleteByExamSessionId(Long examSessionId);
}
