package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.model.LeaveType;
import org.ptithcm2021.hr_management.model.User;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplicationRequest {
    @NotNull(message = "Time off cannot be empty")
    private Date startDate;

    private Date endDate;
    private String reason;

    @NotNull(message = "User cannot be empty")
    private Long userId;

    @NotNull(message = "Leave type cannot be empty")
    private Integer leaveTypeId;
}
