package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionUpdateRequest {
    private String attachment;

    @NotNull(message ="The decision maker cannot leave blank.")
    private long signerId;
}
