package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.commons.constant.ExamSessionStudentStateEnum;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionQuestionSnapshot;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudent;
import edu.exam_online.exam_online_system.entity.exam.ExamSessionStudentAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExamSessionStudentAnswerMapper {

    /**
     * Create student answer from snapshot question
     * This is the primary method for creating student answers
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "examSessionStudent", source = "examSessionStudent")
    @Mapping(target = "examSessionQuestionSnapshot", source = "snapshotQuestion")
    @Mapping(target = "selectedAnswerSnapshot", ignore = true)
    @Mapping(target = "state", source = "state")
    @Mapping(target = "teacherFeedback", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ExamSessionStudentAnswer toEntityFromSnapshot(ExamSessionStudentStateEnum state,
            ExamSessionStudent examSessionStudent, ExamSessionQuestionSnapshot snapshotQuestion);
}
