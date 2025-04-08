package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.LeaveBalanceRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveBalanceResponse;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;

public interface LeaveBalanceService {
    void createLeaveBalance(LeaveBalanceRequest leaveBalanceRequest);

    void dayOff(int year, LeaveApplication leaveApplication);

    void updateLeaveBalance(LeaveBalance leaveBalance);

    LeaveBalance getLeaveBalanceToLeaveBalance(long userId);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    LeaveBalanceResponse getLeaveBalance(long userId);
}
