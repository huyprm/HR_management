package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryRequest {
    @NotNull(message = "userId must not be null")
    private Long userId;
    
    @NotNull(message = "contractId must not be null")
    private Integer contractId;
    
    @NotNull(message = "salaryMonth must not be null")
    private YearMonth salaryMonth;

}