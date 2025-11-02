package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.commons.constant.ExamSessionStudentStateEnum;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudent;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudentAnswer;
import edu.exam_online.exam_online_system.entity.exam.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExamSessionStudentAnswerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "examSessionStudent", source = "examSessionStudent")
    @Mapping(target = "question", source = "question")
    ExamSessionStudentAnswer toEntity(ExamSessionStudentStateEnum state, ExamSessionStudent examSessionStudent, Question question);
}
