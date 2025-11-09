package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.QuestionResponse;
import edu.exam_online.exam_online_system.entity.exam.BankQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankQuestionMapper {

    @Mapping(target = "bankQuestionId", source = "bankQuestion.id")
    @Mapping(target = "questions", source = "questions")
    BankQuestionDetailResponse toDetailResponse(BankQuestion bankQuestion, List<QuestionResponse> questions);

    BankQuestion toEntity(BankQuestionCreationRequest request);

    @Mapping(source = "id", target = "bankQuestionId")
    BankQuestionResponse toResponse(BankQuestion bankQuestion);
}
