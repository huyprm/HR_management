package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.ptithcm2021.hr_management.dto.response.UserSummaryResponse;
import org.ptithcm2021.hr_management.model.User;

@Mapper(componentModel = "spring")
public interface UserSummaryMapper {
    UserSummaryResponse toUserSummaryResponse(User user);
}
