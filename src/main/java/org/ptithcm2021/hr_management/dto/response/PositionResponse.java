package org.ptithcm2021.hr_management.dto.response;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.model.Department;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionResponse {
    private int id;
    private String name;
    private String description;
    private DepartmentResponse department;
}
