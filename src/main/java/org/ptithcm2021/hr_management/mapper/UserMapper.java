package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserRequest userRequest);
    UserResponse toUserResponse (User user);
}
