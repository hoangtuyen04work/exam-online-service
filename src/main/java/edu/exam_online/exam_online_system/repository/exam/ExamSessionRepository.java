package edu.exam_online.exam_online_system.repository.exam;

import edu.exam_online.exam_online_system.entity.exam.ExamSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSessionRepository extends JpaRepository<ExamSession, Long> {

    @Query("SELECT e FROM ExamSession e WHERE e.code = :code")
    Optional<ExamSession> findByCode(@Param("code") String code);

    @Query("""
        SELECT e FROM ExamSession e
        WHERE e.owner.id = :ownerId
        AND (:examId IS NULL OR e.exam.id = :examId)
    """)
    Page<ExamSession> findAllByOwnerIdOrderByCreatedAtDesc(
            @Param("examId") Long examId,
            @Param("ownerId") Long ownerId,
            Pageable pageable);

    Optional<ExamSession> findByIdAndOwnerId(Long id, Long ownerId);
}