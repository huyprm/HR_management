package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.LeaveApplicationRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveApplicationResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface LeaveApplicationService {
    @PreAuthorize("T(String).valueOf(#leaveApplicationRequest.userId) == authentication.name")
    LeaveApplicationResponse createApplication(LeaveApplicationRequest leaveApplicationRequest);

    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    LeaveApplicationResponse confirmApplication (FormStatusEnum formStatusEnum, long applicationId);

    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    List<LeaveApplicationResponse> getApplicationIsPending(FormStatusEnum formStatusEnum);

    LeaveApplicationResponse getApplication(long applicationId);
}
