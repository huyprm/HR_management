package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.response.PayrollResponse;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ReportService {
    List<PayrollResponse> getPayrollByMonth(LocalDate startDate, LocalDate endDate);
}
