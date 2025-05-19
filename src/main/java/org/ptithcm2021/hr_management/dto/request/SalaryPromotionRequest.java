package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryPromotionRequest {
    private String reason;

    @NotNull(message = "currentJobGradeId must not be null")
    private String currentJobGradeId;

    @NotNull(message = "requestJobGradeId must not be null")
    private String requestJobGradeId;

    @NotNull(message = "userId must not be null")
    private Long userId;
}
