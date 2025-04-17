package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryResponse {
    private int id;
    private String salaryMonth;
    private double totalAllowance;
    private double unpaidLeaveDeduction;
    private double baseSalary;
    private double totalSalary; // Lương sau khi tính phụ cấp và khấu trừ
    private String paymentDate;
    private UserSummaryResponse user;
    private ContractSummaryResponse contract;
}