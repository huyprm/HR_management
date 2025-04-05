package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequest {
    @NotBlank(message = "Decision type cannot be empty")
    private String decisionId;

    @NotNull(message ="Recipient cannot be empty")
    private Long userId;
}
