package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.SalaryRequest;
import org.ptithcm2021.hr_management.dto.response.SalaryResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.YearMonth;
import java.util.List;

public interface SalaryService {
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    SalaryResponse createSalary(SalaryRequest salaryRequest);
    
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    void deleteSalary(int id);
    
    SalaryResponse getSalary(int id);
    
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN') or T(String).valueOf(#userId) == authentication.name")
    List<SalaryResponse> getSalariesByUser(long userId);
    
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    List<SalaryResponse> getSalariesByMonth(YearMonth yearMonth);
    
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'systems')")
    void generateMonthlySalaries(YearMonth yearMonth);
    
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    List<SalaryResponse> getAllSalaries();
}