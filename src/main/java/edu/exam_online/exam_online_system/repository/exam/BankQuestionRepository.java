package edu.exam_online.exam_online_system.repository.exam;

import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.BankQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BankQuestionRepository extends JpaRepository<BankQuestion, Long> {

    List<BankQuestion> findByTeacher(User teacher);

    Optional<BankQuestion> findByIdAndTeacherId(Long id, Long teacherId);

    List<BankQuestion> findByNameContainingIgnoreCase(String name);

    Page<BankQuestion> findByTeacherId(Long teacherId, Pageable pageable);

    Optional<BankQuestion> findByTeacherIdAndId(Long teacherId, Long id);
}
