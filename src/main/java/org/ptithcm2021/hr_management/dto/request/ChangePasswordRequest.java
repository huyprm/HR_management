package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotNull
    private Long userId;

    @NotBlank(message = "Old password cannot be empty")
    private String oldPass;

    @NotBlank(message = "New password cannot be empty")
    private String newPass;
}
