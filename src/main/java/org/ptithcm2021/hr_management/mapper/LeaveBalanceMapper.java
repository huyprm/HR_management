package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ptithcm2021.hr_management.dto.request.LeaveBalanceRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveBalanceResponse;
import org.ptithcm2021.hr_management.model.LeaveBalance;

@Mapper(componentModel = "spring")
public interface LeaveBalanceMapper {
    LeaveBalance toLeaveBalance(LeaveBalanceRequest leaveBalanceRequest);

    @Mapping(target = "remainingLeave", expression = "java(leaveBalance.getRemainingLeave())")
    LeaveBalanceResponse toLeaveBalanceResponse(LeaveBalance leaveBalance);
}
