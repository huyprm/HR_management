package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceResponse {
    private int id;
    private long userId;
    private int year;
    private int month;
    private int totalLeaveDay;
    private int carriedOverDay;
    private int usedLeaveDay;
    private int usedBHXH;
    public int remainingLeaveDay;
}
