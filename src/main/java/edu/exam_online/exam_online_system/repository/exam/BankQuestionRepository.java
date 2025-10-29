package edu.exam_online.exam_online_system.repository.exam;

import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.BankQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BankQuestionRepository extends JpaRepository<BankQuestion, Long> {

    List<BankQuestion> findByTeacher(User teacher);

    List<BankQuestion> findByNameContainingIgnoreCase(String name);
}
