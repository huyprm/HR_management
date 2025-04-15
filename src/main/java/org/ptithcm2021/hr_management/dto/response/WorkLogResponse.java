package org.ptithcm2021.hr_management.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.enums.WorkLogTypeEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkLogResponse {
    private WorkLogTypeEnum type;

    @JsonIgnoreProperties("user")
    private ContractResponse contract;

    @JsonIgnoreProperties("user")
    private DecisionResponse decision;
}
