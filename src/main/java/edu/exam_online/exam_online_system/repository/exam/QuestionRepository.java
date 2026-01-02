package edu.exam_online.exam_online_system.repository.exam;

import edu.exam_online.exam_online_system.commons.constant.Difficulty;
import edu.exam_online.exam_online_system.entity.exam.BankQuestion;
import edu.exam_online.exam_online_system.entity.exam.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Tìm các câu hỏi theo ngân hàng câu hỏi
    List<Question> findByBankQuestion(BankQuestion bankQuestion);

    @Query("SELECT q FROM Question q WHERE q.id IN :ids")
    List<Question> findAllByIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT q FROM Question q WHERE q.bankQuestion.id = :bankQuestionId AND " +
            "q.difficulty = :difficulty ORDER BY RAND() LIMIT :quantity")
    List<Question> findByBankQuestionId(@Param("bankQuestionId") Long bankQuestionId,
                                        @Param("difficulty")Difficulty difficulty, @Param("quantity")Long quantity);

}
