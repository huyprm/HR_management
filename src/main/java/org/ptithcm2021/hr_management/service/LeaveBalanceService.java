package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.LeaveBalanceRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveBalanceResponse;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

public interface LeaveBalanceService {
    void createLeaveBalance(LeaveBalanceRequest leaveBalanceRequest);

    // Phương thức mới để cập nhật ngày nghỉ theo tháng
    void dayOff(int year, int month, LeaveApplication leaveApplication);

    void updateLeaveBalance(LeaveBalance leaveBalance);

    // Lấy LeaveBalance theo tháng hiện tại
    LeaveBalance getLeaveBalanceToLeaveBalance(long userId);

    // Lấy tất cả LeaveBalance của một người dùng trong một năm
    List<LeaveBalance> getAllLeaveBalancesByYear(long userId, int year);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    LeaveBalanceResponse getLeaveBalance(long userId);
    
    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    LeaveBalanceResponse getLeaveBalanceByMonth(long userId, int year, int month);
}
