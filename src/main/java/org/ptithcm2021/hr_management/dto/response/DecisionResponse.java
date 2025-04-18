package org.ptithcm2021.hr_management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.ptithcm2021.hr_management.enums.DecisionEnum;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DecisionResponse {
    private String id;
    private String attachment;
    private String content;
    private double value;
    private DecisionEnum type;
    private Date date;
    private UserSummaryResponse signer;
    private UserSummaryResponse user;
    private SeniorityAllowanceRuleResponse seniorityAllowanceRule;
    private SalaryPromotionResponse salaryPromotion;
    private PositionResponse position;
}
