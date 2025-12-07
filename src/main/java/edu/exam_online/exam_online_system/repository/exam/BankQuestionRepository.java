package edu.exam_online.exam_online_system.repository.exam;

import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.BankQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BankQuestionRepository extends JpaRepository<BankQuestion, Long> {

    List<BankQuestion> findByTeacher(User teacher);

    Optional<BankQuestion> findByIdAndTeacherId(Long id, Long teacherId);

    List<BankQuestion> findByNameContainingIgnoreCase(String name);

    @Query("""
        SELECT b
        FROM BankQuestion b
        WHERE b.teacher.id = :teacherId
          AND (:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')))
        ORDER BY b.createdAt DESC
        """)
    Page<BankQuestion> search(
            @Param("teacherId") Long teacherId,
            @Param("name") String name,
            Pageable pageable
    );
    Optional<BankQuestion> findByTeacherIdAndId(Long teacherId, Long id);
}
