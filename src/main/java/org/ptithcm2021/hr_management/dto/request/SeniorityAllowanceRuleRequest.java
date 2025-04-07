package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.model.User;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeniorityAllowanceRuleRequest {
    @NotNull(message = "Min service cannot be empty")
    @Min(value = 0, message = "Minimum value must be greater than or equal to 0")
    private Integer minService;

    @NotNull(message = "Senior percentage cannot be empty")
    private Double seniorityPercentage;

    @NotNull(message = "Senior leave day cannot be empty")
    private Integer seniorityLeaveDay;

    @NotNull(message = "Effective date cannot be empty")
    private Date effectiveDate;

    private Date expiryDate;

    private String description;

    @NotNull(message = "Signer cannot be empty")
    private Long signerId;
}
