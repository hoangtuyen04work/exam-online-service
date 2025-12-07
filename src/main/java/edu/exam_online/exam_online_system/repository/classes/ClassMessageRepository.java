package edu.exam_online.exam_online_system.repository.classes;

import edu.exam_online.exam_online_system.entity.classes.ClassMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassMessageRepository extends JpaRepository<ClassMessage, Long> {

    @Query("SELECT cm FROM ClassMessage cm WHERE cm.classEntity.id = :classId ORDER BY cm.createdAt DESC")
    Page<ClassMessage> findByClassId(@Param("classId") Long classId, Pageable pageable);
}
