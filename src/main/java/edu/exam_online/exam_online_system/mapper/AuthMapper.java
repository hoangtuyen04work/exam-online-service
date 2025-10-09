package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.response.AuthResponse;
import edu.exam_online.exam_online_system.dto.response.RegisterResponse;
import edu.exam_online.exam_online_system.entity.Token;
import edu.exam_online.exam_online_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    RegisterResponse toRegisterResponse(Long userId);

    @Mapping(target = "type", constant = "Bearer")
    AuthResponse toAuthResponse(Token t, User u);

    AuthResponse toAuthResponse(Token t);
}
