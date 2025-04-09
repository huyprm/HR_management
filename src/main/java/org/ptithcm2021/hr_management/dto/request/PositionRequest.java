package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.model.Department;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionRequest {
    @NotBlank(message = "Position code cannot be blank")
    private String id;

    @NotBlank(message = "Position name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Department position cannot be empty")
    private String departmentId;

    @NotNull(message = "Role cannot be empty")
    private RoleEnum roleId;
}
