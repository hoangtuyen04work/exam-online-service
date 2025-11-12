package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.commons.constant.CodeTypeEnum;
import edu.exam_online.exam_online_system.entity.auth.Code;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.utils.TimeUtils;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface CodeMapper {
    default Code toVerificationCode(String code, User user) {
        return Code.builder()
                .code(code)
                .codeType(CodeTypeEnum.REGISTER_CODE)
                .expiresAt(TimeUtils.getCurrentTime().plusMinutes(10))
                .user(user)
                .build();
    }
}
