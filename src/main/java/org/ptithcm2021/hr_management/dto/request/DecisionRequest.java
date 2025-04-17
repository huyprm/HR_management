package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.ptithcm2021.hr_management.model.Position;
import org.ptithcm2021.hr_management.model.SalaryPromotion;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionRequest {
    @NotBlank(message = "Decision code cannot be empty")
    private String id;

    // Dành cho quyết định có giá trị như reward hoặc discipline
    private double value;
    private String content;

    @NotNull(message = "Decision type cannot be empty")
    private DecisionEnum type;

    @NotNull(message = "Decision date cannot be empty")
    private Date date;

    @NotNull(message ="User id cannot be empty")
    private long userId;

    private int salaryPromotionId;

    private String positionId;

    private  int seniorityAllowanceRuleId;
}
