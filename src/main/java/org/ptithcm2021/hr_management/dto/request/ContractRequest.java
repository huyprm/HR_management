package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.ContractType;
import org.ptithcm2021.hr_management.model.JobGrade;
import org.ptithcm2021.hr_management.model.Position;
import org.ptithcm2021.hr_management.model.User;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    @NotNull(message = "Contract start time is indispensable")
    private LocalDate startDate;

    @NotNull(message = "Contract end time is indispensable")
    private LocalDate endDate;

    @NotNull(message = "Starting salary is indispensable")
    @Min(value = 0, message = "Minimum value must be greater than 0")
    private Double basicSalary;

    private String clause;

    @NotBlank(message = "Contract type is indispensable")
    private String contractTypeId;

    @NotNull(message = "Indispensable contractor")
    private Long userId;

    @NotNull(message = "Indispensable contractor")
    private Long signerId;

    @NotBlank(message = "Position is indispensable")
    private String positionId;

    @NotBlank(message = "Job grade is indispensable")
    private String jobGradeId;
}
