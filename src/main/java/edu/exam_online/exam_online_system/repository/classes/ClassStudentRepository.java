package edu.exam_online.exam_online_system.repository.classes;

import edu.exam_online.exam_online_system.entity.classes.ClassStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudent, Long> {

    @Query("SELECT cs FROM ClassStudent cs WHERE cs.classEntity.id = :classId")
    List<ClassStudent> findByClassId(@Param("classId") Long classId);

    @Query("SELECT cs FROM ClassStudent cs WHERE cs.classEntity.id = :classId AND cs.student.id = :studentId")
    Optional<ClassStudent> findByClassIdAndStudentId(@Param("classId") Long classId,
            @Param("studentId") Long studentId);

    @Query("SELECT cs FROM ClassStudent cs WHERE cs.student.id = :studentId")
    List<ClassStudent> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT cs FROM ClassStudent cs WHERE cs.student.id = :studentId")
    org.springframework.data.domain.Page<ClassStudent> findByStudentId(@Param("studentId") Long studentId,
            org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Query("DELETE FROM ClassStudent cs WHERE cs.classEntity.id = :classId AND cs.student.id = :studentId")
    void deleteByClassIdAndStudentId(@Param("classId") Long classId, @Param("studentId") Long studentId);

    boolean existsByClassEntityIdAndStudentId(Long classId, Long studentId);
}
