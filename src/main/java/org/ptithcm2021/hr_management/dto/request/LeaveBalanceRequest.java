package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.model.User;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveBalanceRequest {
    private int totalLeaveDay;
    private int carriedOverDay;
    private int usedLeaveDay;
    private long userId;
    @Builder.Default
    private int year = Year.now().getValue();
    @Builder.Default
    private int month = LocalDate.now().getMonthValue();
}
