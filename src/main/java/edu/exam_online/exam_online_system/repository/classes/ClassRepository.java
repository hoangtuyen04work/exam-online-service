package edu.exam_online.exam_online_system.repository.classes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<edu.exam_online.exam_online_system.entity.classes.Class, Long> {

    @Query("SELECT c FROM Class c WHERE c.teacher.id = :teacherId")
    Page<edu.exam_online.exam_online_system.entity.classes.Class> findByTeacherId(@Param("teacherId") Long teacherId,
            Pageable pageable);

    @Query("SELECT c FROM Class c WHERE c.id = :id AND c.teacher.id = :teacherId")
    Optional<edu.exam_online.exam_online_system.entity.classes.Class> findByIdAndTeacherId(@Param("id") Long id,
            @Param("teacherId") Long teacherId);

    boolean existsByClassCode(String classCode);

    Optional<edu.exam_online.exam_online_system.entity.classes.Class> findByClassCode(String classCode);
}
