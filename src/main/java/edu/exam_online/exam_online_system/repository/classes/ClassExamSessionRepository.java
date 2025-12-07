package edu.exam_online.exam_online_system.repository.classes;

import edu.exam_online.exam_online_system.entity.classes.ClassExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassExamSessionRepository extends JpaRepository<ClassExamSession, Long> {

    @Query("SELECT ces FROM ClassExamSession ces WHERE ces.classEntity.id = :classId")
    List<ClassExamSession> findByClassId(@Param("classId") Long classId);

    @Query("SELECT ces FROM ClassExamSession ces WHERE ces.classEntity.id = :classId AND ces.examSession.id = :examSessionId")
    Optional<ClassExamSession> findByClassIdAndExamSessionId(@Param("classId") Long classId, @Param("examSessionId") Long examSessionId);

    @Query("SELECT ces FROM ClassExamSession ces WHERE ces.examSession.id = :examSessionId")
    List<ClassExamSession> findByExamSessionId(@Param("examSessionId") Long examSessionId);

    @Modifying
    @Query("DELETE FROM ClassExamSession ces WHERE ces.classEntity.id = :classId AND ces.examSession.id = :examSessionId")
    void deleteByClassIdAndExamSessionId(@Param("classId") Long classId, @Param("examSessionId") Long examSessionId);

    boolean existsByClassEntityIdAndExamSessionId(Long classId, Long examSessionId);
}
