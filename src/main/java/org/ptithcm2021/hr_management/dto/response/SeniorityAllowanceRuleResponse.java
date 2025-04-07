package org.ptithcm2021.hr_management.dto.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.model.User;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeniorityAllowanceRuleResponse {
    private int id;
    private int minService;
    private double seniorityPercentage;
    private int seniorityLeaveDay;
    private Date effectiveDate;
    private Date expiryDate;
    private String description;
    private UserSummaryResponse signer;
}
