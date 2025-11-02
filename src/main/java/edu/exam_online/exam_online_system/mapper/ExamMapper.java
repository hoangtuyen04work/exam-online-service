package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.request.exam.ExamCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamUpdateQuestionsRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.QuestionResponse;
import edu.exam_online.exam_online_system.entity.exam.Exam;
import edu.exam_online.exam_online_system.entity.exam.Question;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    void updateEntity(@MappingTarget Exam exam, ExamUpdateQuestionsRequest request);

    Exam toEntity(ExamCreationRequest request);

    @Mapping(target = "examId", source = "id")
    ExamResponse toResponse(Exam exam);

    @Mapping(source = "questionExams", target = "questions")
    ExamDetailResponse toExamDetailResponse(Exam exam);

    @Mapping(source = "exam.id", target = "examId")
    ExamDetailResponse toExamDetailResponse(Exam exam, List<QuestionResponse> questions);

    void updateExamFromRequest(ExamUpdateRequest request, @MappingTarget Exam exam);

    @AfterMapping
    default void makeMutable(@MappingTarget Exam exam) {
        if (exam.getQuestionExams() != null) {
            exam.setQuestionExams(new ArrayList<>(exam.getQuestionExams()));
        }
    }
    @AfterMapping
    default void linkAnswers(@MappingTarget Question question) {
        if (question.getAnswers() != null) {
            question.getAnswers().forEach(answer -> answer.setQuestion(question));
        }
    }
}
