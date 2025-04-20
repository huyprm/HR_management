package org.ptithcm2021.hr_management.dto.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.ContractType;
import org.ptithcm2021.hr_management.model.JobGrade;
import org.ptithcm2021.hr_management.model.Position;
import org.ptithcm2021.hr_management.model.User;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractResponse {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private double basicSalary;
    private String clause;
    private ContractStatusEnum contractStatusEnum;
    private String contractTypeName ;
    private UserSummaryResponse user;
    private UserSummaryResponse signer;
    private String positionName;
    private String jobGradeName;
}
