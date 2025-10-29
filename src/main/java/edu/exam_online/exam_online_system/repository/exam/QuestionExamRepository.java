package edu.exam_online.exam_online_system.repository.exam;


import edu.exam_online.exam_online_system.entity.exam.Exam;
import edu.exam_online.exam_online_system.entity.exam.Question;
import edu.exam_online.exam_online_system.entity.exam.QuestionExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionExamRepository extends JpaRepository<QuestionExam, Long> {

    // Lấy danh sách câu hỏi trong một kỳ thi
    List<QuestionExam> findByExam(Exam exam);

    // Kiểm tra xem một câu hỏi đã nằm trong kỳ thi chưa
    boolean existsByExamAndQuestion(Exam exam, Question question);

    // Lấy theo question
    List<QuestionExam> findByQuestion(Question question);
}
