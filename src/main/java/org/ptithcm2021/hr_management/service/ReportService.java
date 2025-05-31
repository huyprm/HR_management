package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.response.ContractExpireReportResponse;
import org.ptithcm2021.hr_management.dto.response.PayrollResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ReportService {
    List<PayrollResponse> getPayrollByMonth(LocalDate startDate, LocalDate endDate);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF', 'SCOPE_MANAGER')")
    List<ContractExpireReportResponse> getExpiringContracts(int days);


}
