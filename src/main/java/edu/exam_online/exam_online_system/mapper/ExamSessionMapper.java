package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamSessionUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamSessionResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.Exam;
import edu.exam_online.exam_online_system.entity.exam.ExamSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamSessionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ExamSession toEntity(ExamSessionCreationRequest request, Exam exam, User owner, String code);

    @Mapping(target = "examSessionId", source = "examSession.id")
    @Mapping(target = "ownerName", source = "examSession.owner.username")
    ExamSessionResponse toResponse(ExamSession examSession, String inviteLink);

    void updateEntity(@MappingTarget ExamSession examSession, ExamSessionUpdateRequest request);

    List<ExamSessionResponse> toResponse(List<ExamSession> examSessions);
}
