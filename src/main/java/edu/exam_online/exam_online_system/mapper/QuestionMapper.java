package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.request.exam.QuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.QuestionUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.QuestionResponse;
import edu.exam_online.exam_online_system.entity.exam.Answer;
import edu.exam_online.exam_online_system.entity.exam.Question;
import edu.exam_online.exam_online_system.entity.exam.QuestionExam;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AnswerMapper.class})
public interface QuestionMapper {
    default void updateEntity(Question question, QuestionUpdateRequest qReq){
        question.setContent(qReq.getContent());
        question.setDifficulty(qReq.getDifficulty());
        question.setExplanation(qReq.getExplanation());
        question.setShuffleAnswers(qReq.isShuffleAnswers());
        question.setShuffleQuestions(qReq.isShuffleQuestions());
    }

    Question toEntity(QuestionUpdateRequest request);

    Question toEntity(QuestionCreationRequest request);

    Question toEntity(QuestionCreationRequest request, List<Answer> answers);

    @Mapping(source = "questionExam.question.id", target = "questionId")
    @Mapping(source = "questionExam.question.content", target = "content")
    @Mapping(source = "questionExam.question.difficulty", target = "difficulty")
    @Mapping(source = "questionExam.question.explanation", target = "explanation")
    @Mapping(source = "questionExam.question.shuffleAnswers", target = "shuffleAnswers")
    @Mapping(source = "questionExam.question.shuffleQuestions", target = "shuffleQuestions")
    @Mapping(source = "questionExam.question.answers", target = "answers")
    @Mapping(source = "questionExam.point", target = "point")
    @Mapping(source = "questionExam.orderColumn", target = "orderColumn")
    QuestionResponse toQuestionResponse(QuestionExam questionExam);

    List<QuestionResponse> toResponse(List<Question> questions);

    @Mapping(source = "id", target = "questionId")
    QuestionResponse toResponse(Question question);

    List<QuestionResponse> toQuestionResponseList(List<QuestionExam> questionExams);

    @AfterMapping
    default void linkAnswers(@MappingTarget Question question) {
        if (question.getAnswers() != null) {
            question.getAnswers().forEach(answer -> answer.setQuestion(question));
        }
    }

}
