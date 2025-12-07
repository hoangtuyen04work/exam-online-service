package edu.exam_online.exam_online_system.repository;

import edu.exam_online.exam_online_system.entity.exam.ExamSessionAnswerSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSessionAnswerSnapshotRepository extends JpaRepository<ExamSessionAnswerSnapshot, Long> {

    List<ExamSessionAnswerSnapshot> findByExamSessionQuestionSnapshotIdOrderByAnswerOrderAsc(
            Long examSessionQuestionSnapshotId);

    void deleteByExamSessionQuestionSnapshotId(Long examSessionQuestionSnapshotId);
}
