package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.commons.constant.CodeTypeEnum;
import edu.exam_online.exam_online_system.entity.auth.Code;
import edu.exam_online.exam_online_system.entity.auth.User;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface CodeMapper {
    default Code toVerificationCode(String code, User user) {
        return Code.builder()
                .code(code)
                .codeType(CodeTypeEnum.REGISTER_CODE)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();
    }
}
