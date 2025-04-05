package org.ptithcm2021.hr_management.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.model.User;

import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceResponse {
    private int id;
    private int year;
    private int totalLeaveDay;
    private int carriedOverDay;
    private int usedLeaveDay;
    public int remainingLeave;
}
