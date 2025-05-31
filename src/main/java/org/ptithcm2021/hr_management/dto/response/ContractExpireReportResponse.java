package org.ptithcm2021.hr_management.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContractExpireReportResponse {
    private int stt;
    private String fullName;
    private String email;
    private String departmentName;
    private String positionName;
    private String contractTypeName;
    private LocalDate endDate;
    private int remainingDays;
    private String contractStatus;
}