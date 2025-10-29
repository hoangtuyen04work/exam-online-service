package edu.exam_online.exam_online_system.repository.exam;


import edu.exam_online.exam_online_system.entity.exam.Answer;
import edu.exam_online.exam_online_system.entity.exam.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // Lấy tất cả đáp án thuộc một câu hỏi
    List<Answer> findByQuestion(Question question);

}
