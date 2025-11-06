package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.commons.constant.ExamSessionStudentStateEnum;
import edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum;
import edu.exam_online.exam_online_system.dto.request.exam.TeacherFeedBackRequest;
import edu.exam_online.exam_online_system.dto.request.exam.TeacherOverallFeedBackRequest;
import edu.exam_online.exam_online_system.dto.response.exam.student.AnswerContentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.ExamSessionContentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.ExamSessionStudentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.JoinExamSessionResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.QuestionContentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.result.AnswerResultResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.result.ExamSessionStudentResultResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.result.QuestionResultResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.StudentJoinedExamSessionResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.Answer;
import edu.exam_online.exam_online_system.entity.exam.ExamSession;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudent;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudentAnswer;
import edu.exam_online.exam_online_system.entity.exam.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ExamSessionStudentMapper {

    default void updateEntity(ExamSessionStudent examSessionStudent, TeacherOverallFeedBackRequest request){
        examSessionStudent.setTeacherOverallFeedback(request.getTeacherOverallFeedBack());

        Map<Long, String> questionIdToTeacherFeedBack = request.getTeacherFeedBackRequests().stream()
                        .collect(Collectors.toMap(TeacherFeedBackRequest::getQuestionId, TeacherFeedBackRequest::getTeacherFeedBack));
        examSessionStudent.getAnswers()
                .forEach(answer ->{answer.setTeacherFeedback(questionIdToTeacherFeedBack.get(answer.getQuestion().getId()));});
    }

    default StudentJoinedExamSessionResponse toJoinedResponse(ExamSessionStudent examSessionStudent){
        return StudentJoinedExamSessionResponse.builder()
                .examSessionStudentId(examSessionStudent.getId())
                .studentId(examSessionStudent.getStudent().getId())
                .score(examSessionStudent.getTotalScore())
                .status(examSessionStudent.getStatus())
                .submittedAt(examSessionStudent.getSubmittedAt() == null ? null : examSessionStudent.getSubmittedAt())
                .studentName(examSessionStudent.getStudent().getUsername())
                .build();
    }

    default ExamSessionStudentResultResponse toResponse(ExamSessionStudent examSessionStudent){
        return ExamSessionStudentResultResponse.builder()
                .examSessionId(examSessionStudent.getExamSession().getId())
                .examSessionName(examSessionStudent.getExamSession().getName())
                .totalScore(examSessionStudent.getTotalScore())
                .status(examSessionStudent.getStatus())
                .submittedAt(examSessionStudent.getSubmittedAt())
                .teacherOverallFeedback(examSessionStudent.getTeacherOverallFeedback())
                .questions(mapQuestions(examSessionStudent))
                .build();
    }

    default List<QuestionResultResponse> mapQuestions(ExamSessionStudent examSessionStudent) {
        if (examSessionStudent == null || examSessionStudent.getAnswers() == null) {
            return Collections.emptyList();
        }

        Map<Long, List<ExamSessionStudentAnswer>> questionIdToAnswerIds = examSessionStudent.getAnswers()
                .stream()
                .filter(Objects::nonNull)
                .filter(a -> a.getQuestion() != null)
                .collect(Collectors.groupingBy(e -> e.getQuestion().getId()));

        Map<Long, Question> idToQuestion = examSessionStudent.getAnswers().stream()
                .collect(Collectors.toMap(
                        examSessionStudentAnswer -> examSessionStudentAnswer.getQuestion().getId(),   // key: ID của câu hỏi
                        ExamSessionStudentAnswer::getQuestion                       // value: đối tượng Question
                ));

        return questionIdToAnswerIds.entrySet().stream()
                .map(entry -> mapQuestion(idToQuestion.get(entry.getKey()), entry.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    default QuestionResultResponse mapQuestion(Question question, List<ExamSessionStudentAnswer> studentAnswers) {
        if (question == null) return null;
        QuestionResultResponse qdto = new QuestionResultResponse();
        qdto.setQuestionId(question.getId());
        qdto.setContent(question.getContent());
        qdto.setExplanation(question.getExplanation());

        List<AnswerResultResponse> answers = Optional.ofNullable(question.getAnswers())
                .orElse(Collections.emptyList())
                .stream()
                .map(answer -> mapAnswer(answer, studentAnswers))
                .collect(Collectors.toList());

        qdto.setAnswers(answers);
        return qdto;
    }

    default AnswerResultResponse mapAnswer(Answer answer, List<ExamSessionStudentAnswer> studentAnswers) {
        if (answer == null) return null;

        AnswerResultResponse adto = new AnswerResultResponse();
        adto.setAnswerId(answer.getId());
        adto.setContent(answer.getContent());
        adto.setCorrect(answer.isCorrect());

        boolean selected = false;
        if (studentAnswers != null && !studentAnswers.isEmpty()) {
            selected = studentAnswers.stream()
                    .map(ExamSessionStudentAnswer::getSelectedAnswer)
                    .filter(Objects::nonNull)
                    .map(Answer::getId)
                    .anyMatch(id -> id != null && id.equals(answer.getId()));
        }
        adto.setSelected(selected);

        return adto;
    }

    @Mapping(target = "examSessionId", source = "examSession.id")
    @Mapping(target = "examSessionName", source = "examSession.name")
    @Mapping(target = "totalScore", source = "totalScore")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "submittedAt", source = "submittedAt")
    ExamSessionStudentResponse toDto(ExamSessionStudent entity);

    default ExamSessionStudent toEntity(ExamSession examSession, User student){
        Integer durationMinutes = examSession.getDurationMinutes();
        return ExamSessionStudent.builder()
                .examSession(examSession)
                .student(student)
                .expiredAt(LocalDateTime.now().plusMinutes(durationMinutes))
                .build();
    }

    @Mapping(target = "examSessionId", source = "examSession.id")
    JoinExamSessionResponse toResponse(ExamSession examSession, ExamSessionStudentStateEnum state);

    @Mapping(target = "examSessionId", source = "examSessionStudent.examSession.id")
    @Mapping(target = "questions", ignore = true)
    ExamSessionContentResponse toDoneResponse(ExamSessionStudent examSessionStudent, ExamStudentStatusEnum status);

    default ExamSessionContentResponse toDraftResponse(ExamSessionStudent examSessionStudent, ExamStudentStatusEnum status){
        return ExamSessionContentResponse.builder()
                .examSessionId(examSessionStudent.getExamSession().getId())
                .status(status)
                .name(examSessionStudent.getExamSession().getName())
                .questions(toQuestionContentResponse(examSessionStudent.getAnswers()))
                .build();
    }

    default ExamSessionContentResponse toNewResponse(ExamSessionStudent examSessionStudent){
        return ExamSessionContentResponse.builder()
                .examSessionId(examSessionStudent.getExamSession().getId())
                .name(examSessionStudent.getExamSession().getName())
                .questions(toQuestionContentResponse(examSessionStudent.getAnswers()))
                .build();
    }

    default List<QuestionContentResponse> toQuestionContentResponse(List<ExamSessionStudentAnswer> answers){
        return answers.stream().map(this::toQuestionContentResponse).toList();
    }

    default QuestionContentResponse toQuestionContentResponse(ExamSessionStudentAnswer answer){
        return QuestionContentResponse.builder()
                .questionId(answer.getQuestion().getId())
                .content(answer.getQuestion().getContent())
                .answers(toAnswerContentResponse(answer))
                .build();
    }

    default List<AnswerContentResponse> toAnswerContentResponse(ExamSessionStudentAnswer answers){
        List<AnswerContentResponse> response = new ArrayList<>();
        answers.getQuestion().getAnswers().forEach(answer ->{
            AnswerContentResponse a = AnswerContentResponse.builder()
                    .answerId(answer.getId())
                    .content(answer.getContent())
                    .isSelected(answers.getSelectedAnswer() != null && answers.getSelectedAnswer().getId().equals(answer.getId()))
                    .build();
            response.add(a);
        });
        return response;
    }

}
