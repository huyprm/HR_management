package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.ContractExpireReportResponse;
import org.ptithcm2021.hr_management.dto.response.PayrollResponse;
import org.ptithcm2021.hr_management.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/payroll")
    public ApiResponse<List<PayrollResponse>> getPayroll(@RequestParam LocalDate startDate,
                                                         @RequestParam LocalDate endDate) {
        return ApiResponse.<List<PayrollResponse>>builder().data(reportService.getPayrollByMonth(startDate,endDate)).build();
    }
    @GetMapping("/contracts/expiring")
    public ApiResponse<List<ContractExpireReportResponse>> getExpiringContracts(
            @RequestParam(required = false, defaultValue = "30") int days
    ) {
        return ApiResponse.<List<ContractExpireReportResponse>>builder().data(reportService.getExpiringContracts(days)).build();
    }
}
