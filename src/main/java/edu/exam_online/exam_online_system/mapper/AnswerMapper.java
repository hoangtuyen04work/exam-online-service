package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.request.exam.AnswerCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.AnswerUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.AnswerResponse;
import edu.exam_online.exam_online_system.entity.exam.Answer;
import edu.exam_online.exam_online_system.entity.exam.Question;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    void updateEntity(@MappingTarget Answer answer, AnswerUpdateRequest request);

    @Mapping(target = "content", source = "request.content")
    @Mapping(target = "question", source = "question")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isCorrect", source = "request.correct")
    Answer toEntity(AnswerCreationRequest request, Question question);


    @Mapping(target = "content", source = "request.content")
    @Mapping(target = "question", source = "question")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isCorrect", source = "request.correct")
    Answer toEntity(AnswerUpdateRequest request, Question question);

    @Mapping(target = "answerId", source = "id")
    AnswerResponse toAnswerResponse(Answer answer);

    @Mapping(target = "id", source = "answerId")
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "createdBy", expression = "java(teacherId)")
    Answer toEntity(AnswerUpdateRequest dto, @Context Long teacherId);

    @AfterMapping
    default void linkAnswers(@MappingTarget Question question) {
        if (question.getAnswers() != null) {
            question.getAnswers().forEach(answer -> answer.setQuestion(question));
        }
    }
}
