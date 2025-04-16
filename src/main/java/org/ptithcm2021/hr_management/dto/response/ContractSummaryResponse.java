package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractSummaryResponse {
    private int id;
    private String contractType;
    private double basicSalary;
    private String jobGradeName;
    private double coefficient;
}