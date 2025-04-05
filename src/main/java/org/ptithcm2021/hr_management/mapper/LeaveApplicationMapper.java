package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ptithcm2021.hr_management.dto.request.LeaveApplicationRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveApplicationResponse;
import org.ptithcm2021.hr_management.model.LeaveApplication;

@Mapper(componentModel = "spring")
public interface LeaveApplicationMapper {
    LeaveApplication toLeaveApplication(LeaveApplicationRequest leaveApplicationRequest);

    @Mapping(source = "leaveType.name", target = "leaveTypeName")
    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source ="user.fullName", target = "user.fullName")
    LeaveApplicationResponse toLeaveTypeApplicationResponse (LeaveApplication leaveApplication);
}
