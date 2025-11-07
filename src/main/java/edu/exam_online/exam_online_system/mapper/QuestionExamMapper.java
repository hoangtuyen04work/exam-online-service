package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.request.exam.QuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.QuestionUpdateRequest;
import edu.exam_online.exam_online_system.entity.exam.Exam;
import edu.exam_online.exam_online_system.entity.exam.Question;
import edu.exam_online.exam_online_system.entity.exam.QuestionExam;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface QuestionExamMapper {

    @Mapping(target = "id", ignore = true)
    QuestionExam toEntity(Exam exam, Question question);

    QuestionExam toEntity(QuestionUpdateRequest request);

    QuestionExam toEntity(QuestionCreationRequest request);

    default QuestionExam toEntity(Exam exam, QuestionUpdateRequest qReq, Question question){
        return QuestionExam.builder()
                .exam(exam)
                .question(question)
                .point(qReq.getPoint())
                .orderColumn(qReq.getOrderColumn())
                .build();
    }

    @AfterMapping
    default void makeMutable(@MappingTarget Exam exam) {
        if (exam.getQuestionExams() != null) {
            exam.setQuestionExams(new ArrayList<>(exam.getQuestionExams()));
        }
    }
}
