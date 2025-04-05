package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeRequest {
    @NotBlank(message = "Leave type name cannot be blank")
    private String name;
    private String description;
}
