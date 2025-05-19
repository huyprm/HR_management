package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    private String departmentId;

    @NotNull(message = "Role must be selected")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleId;
}
