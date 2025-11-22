package edu.exam_online.exam_online_system.entity.exam;

import edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.utils.TimeUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "exam_session_students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSessionStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_session_id", nullable = false)
    private ExamSession examSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Builder.Default
    @OneToMany(mappedBy = "examSessionStudent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamSessionStudentAnswer> answers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", length = 32)
    private ExamStudentStatusEnum status = ExamStudentStatusEnum.IN_PROGRESS; // , IN_PROGRESS, COMPLETED

    @Column(name = "started_at")
    @Builder.Default
    private OffsetDateTime startedAt = TimeUtils.getCurrentTime();

    @Column(name = "submitted_at")
    private OffsetDateTime submittedAt;

    @Column(name = "teacher_overall_feedback", columnDefinition = "TEXT")
    private String teacherOverallFeedback;

    @Column(name = "expired_at")
    private OffsetDateTime expiredAt;

    @Column(name = "exit_count")
    @Builder.Default
    private Integer exitCount = 0;

    @Column(name = "total_score")
    @Builder.Default
    private Float totalScore = 0F;

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
