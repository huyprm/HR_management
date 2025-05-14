package org.ptithcm2021.hr_management.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import org.mapstruct.*;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.request.UserUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserRequest userRequest);

    @Mapping(target = "departmentName", expression ="java(mapDepartmentName(user))")
    @Mapping(target = "positionName", expression = "java(mapPositionName(user))")
    UserResponse toUserResponse (User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserUpdateRequest userRequest);

    default String mapDepartmentName(User user) {
        if (user.getPosition() != null && user.getPosition().getDepartment() != null) {
            return user.getPosition().getDepartment().getName();
        }
        return null;
    }

    default String mapPositionName(User user) {
        if (user.getPosition() != null) {
            return user.getPosition().getName();
        }
        return null;
    }

}
