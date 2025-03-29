package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ptithcm2021.hr_management.model.Department;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionRequest {
    private String name;
    private String description;

    @NotNull(message = "Department position cannot be empty")
    private int departmentId;
}
