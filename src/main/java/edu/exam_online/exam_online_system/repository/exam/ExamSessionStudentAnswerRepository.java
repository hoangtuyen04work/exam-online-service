package edu.exam_online.exam_online_system.repository.exam;

import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamSessionStudentAnswerRepository extends JpaRepository<ExamSessionStudentAnswer, Long> {
}