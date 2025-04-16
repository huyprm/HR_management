package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryPromotionUpdateRequest {
    @NotNull(message = "id must not be null")
    private Long signerId;

    @NotNull(message = "status must not be null")
    private FormStatusEnum formStatus;

    @NotBlank(message = "reason must not be blank")
    private String reason;
}
