package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollResponse {
    private int workDays;
    private double actualWorkDays;
    private double unpaidLeaveDays;
    private double salary;
    private double seniority;
    private long userId;
    private String fullName;
}
