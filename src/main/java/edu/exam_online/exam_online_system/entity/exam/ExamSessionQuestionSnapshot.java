package edu.exam_online.exam_online_system.entity.exam;

import edu.exam_online.exam_online_system.commons.constant.Difficulty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Snapshot of question content at the time exam session is created
 * This prevents changes to original questions from affecting active exam
 * sessions
 */
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "exam_session_question_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSessionQuestionSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_session_id", nullable = false)
    private ExamSession examSession;

    @Column(name = "original_question_id", nullable = false)
    private Long originalQuestionId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "shuffle_answers")
    @Builder.Default
    private boolean shuffleAnswers = false;

    @Column(name = "shuffle_questions")
    @Builder.Default
    private boolean shuffleQuestions = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "question_order")
    private Integer questionOrder;

    @Builder.Default
    @OneToMany(mappedBy = "examSessionQuestionSnapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamSessionAnswerSnapshot> examSessionAnswerSnapshots = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "examSessionQuestionSnapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamSessionStudentAnswer> studentAnswers = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

}
