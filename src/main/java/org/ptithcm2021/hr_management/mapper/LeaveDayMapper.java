package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.LeaveDayRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveDayResponse;
import org.ptithcm2021.hr_management.model.LeaveDay;

@Mapper(componentModel = "spring")
public interface LeaveDayMapper {
    LeaveDay toLeaveDay(LeaveDayRequest leaveDayRequest);

    LeaveDayResponse toLeaveDayResponse(LeaveDay leaveDay);

    void updateLeaveDay(@MappingTarget LeaveDay leaveDay, LeaveDayRequest leaveDayRequest);
}
