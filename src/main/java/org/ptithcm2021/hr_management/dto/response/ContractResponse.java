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

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractResponse {
    private int id;
    private Date startDate;
    private Date endDate;
    private double basicSalary;
    private String clause;
    private ContractStatusEnum contractStatusEnum;
    private String contractTypeName ;
    private UserSummaryResponse userSummaryResponse;
    private UserSummaryResponse signerSummaryResponse;
    private String positionName;
    private String jobGradeName;
}
