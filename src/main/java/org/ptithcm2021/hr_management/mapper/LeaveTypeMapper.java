package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.LeaveTypeRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveTypeResponse;
import org.ptithcm2021.hr_management.model.LeaveType;

@Mapper(componentModel = "spring")
public interface LeaveTypeMapper {
    LeaveType toLeaveType(LeaveTypeRequest leaveTypeRequest);
    LeaveTypeResponse toLeaveTypeResponse(LeaveType leaveType);

    void updateLeaveType(@MappingTarget LeaveType leaveType, LeaveTypeRequest leaveTypeRequest);
}
