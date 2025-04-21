package org.ptithcm2021.hr_management.dto.response;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryResponse {
    private int id;
    private double baseSalary;
    private double allowance;
    private int numberOfWorkingDays;
    private int numberOfLeaveDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate paymentDate;
    private UserSummaryResponse user;
}