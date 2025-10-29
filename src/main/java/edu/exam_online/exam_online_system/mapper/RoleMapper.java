package edu.exam_online.exam_online_system.mapper;

import edu.exam_online.exam_online_system.dto.response.auth.RoleResponse;
import edu.exam_online.exam_online_system.entity.auth.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    default RoleResponse toResponse(Role role){
        return RoleResponse.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .description(role.getDescription())
                .build();
    }
}
