package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.LeaveDayRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveDayResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.MonthDay;
import java.time.YearMonth;
import java.util.List;

public interface LeaveDayService {
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_STAFF')")
    LeaveDayResponse createLeaveDay(LeaveDayRequest leaveDayRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_STAFF')")
    LeaveDayResponse updateLeaveDay(LeaveDayRequest leaveDayRequest , int leaveDayId);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_STAFF')")
    void deleteLeaveDay(int leaveDayId);

    LeaveDayResponse getLeaveDay(int leaveDayId);

    List<LeaveDayResponse> getListLeaveDayByMonth(YearMonth yearMonth);
}
