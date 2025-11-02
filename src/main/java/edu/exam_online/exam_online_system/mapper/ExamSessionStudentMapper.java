package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.commons.constant.ExamSessionStudentStateEnum;
import edu.exam_online.exam_online_system.commons.constant.ExamStudentStatusEnum;
import edu.exam_online.exam_online_system.dto.response.exam.student.AnswerContentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.ExamSessionContentResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.JoinExamSessionResponse;
import edu.exam_online.exam_online_system.dto.response.exam.student.QuestionContentResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.ExamSession;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudent;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudentAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamSessionStudentMapper {

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
