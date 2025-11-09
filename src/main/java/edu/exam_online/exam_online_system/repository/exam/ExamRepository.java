package edu.exam_online.exam_online_system.repository.exam;

import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    // Lấy tất cả kỳ thi của một giáo viên
    List<Exam> findByTeacher(User teacher);

    // Tìm theo tên kỳ thi
    List<Exam> findByNameContainingIgnoreCase(String name);

    Page<Exam> findByTeacherIdOrderByCreatedAtDesc(Long teacherId, Pageable pageable);

    Optional<Exam> findByIdAndTeacherId(Long id, Long teacherId);

    void deleteByIdAndTeacherId(Long id, Long teacherId);
}
