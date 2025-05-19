package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.LeaveApplicationRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveApplicationResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.util.List;

public interface LeaveApplicationService {
    @PreAuthorize("T(String).valueOf(#leaveApplicationRequest.userId) == authentication.name")
    LeaveApplicationResponse createApplication(LeaveApplicationRequest leaveApplicationRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN')")
    LeaveApplicationResponse confirmApplication (FormStatusEnum formStatusEnum, long applicationId);

    @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN')")
    List<LeaveApplicationResponse> getApplicationIsPending(String departmentId, FormStatusEnum formStatusEnum);

    LeaveApplicationResponse getApplication(long applicationId);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    List<LeaveApplicationResponse> getApplicationByUserId(long userId);

    double getTotalLeaveDaysByUserId(long userId, LocalDate startDate, LocalDate endDate);
}
