package edu.exam_online.exam_online_system.entity.exam;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Snapshot of answer content at the time exam session is created
 * This prevents changes to original answers from affecting active exam sessions
 */
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "exam_session_answer_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSessionAnswerSnapshot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_session_question_snapshot_id", nullable = false)
    private ExamSessionQuestionSnapshot examSessionQuestionSnapshot;

    @Column(name = "original_answer_id", nullable = false)
    private Long originalAnswerId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @Column(name = "answer_order")
    private Integer answerOrder;

    @Builder.Default
    @OneToMany(mappedBy = "selectedAnswerSnapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamSessionStudentAnswer> studentAnswers = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

}
