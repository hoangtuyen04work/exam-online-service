package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.QuestionResponse;
import edu.exam_online.exam_online_system.entity.exam.BankQuestion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankQuestionMapper {

    BankQuestionDetailResponse toDetailResponse(BankQuestion bankQuestion, List<QuestionResponse> questions);

    BankQuestion toEntity(BankQuestionCreationRequest request);

    BankQuestionResponse toResponse(BankQuestion bankQuestion);
}
