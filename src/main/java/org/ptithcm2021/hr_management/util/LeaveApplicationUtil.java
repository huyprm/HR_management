package org.ptithcm2021.hr_management.util;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.repository.LeaveApplicationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public final class LeaveApplicationUtil {
    private final LeaveApplicationRepository leaveApplicationRepository;

    public double calculateLeveDays(long userId, LocalDate startDate, LocalDate endDate) {
        double used = leaveApplicationRepository.findApprovedLeavesByUserAndMonth(userId, startDate, endDate)
                .stream()
                .filter(leaveApplication -> leaveApplication.getLeaveType().isAffectLeaveBalance())
                .mapToInt(leaveApplication -> {

                    LocalDate startDateOfLeave = leaveApplication.getStartDate();
                    LocalDate endDateOfLeave = leaveApplication.getEndDate();

                    LocalDate effectiveStart  = startDateOfLeave.isBefore(startDate) ? startDate : startDateOfLeave;
                    LocalDate effectiveEnd = endDateOfLeave.isAfter(endDate) ? endDate : endDateOfLeave;

                    long days = ChronoUnit.DAYS.between(effectiveStart, effectiveEnd);

                    return (int) days + 1;
                })
                .sum();
        return used;
    }

    public double calculateTotalLeveDays(long userId, LocalDate startDate, LocalDate endDate) {
        return leaveApplicationRepository.findApprovedLeavesByUserAndMonth(userId, startDate, endDate)
                .stream()
                .mapToInt(leaveApplication -> {

                    LocalDate startDateOfLeave = leaveApplication.getStartDate();
                    LocalDate endDateOfLeave = leaveApplication.getEndDate();

                    LocalDate effectiveStart  = startDateOfLeave.isBefore(startDate) ? startDate : startDateOfLeave;
                    LocalDate effectiveEnd = endDateOfLeave.isAfter(endDate) ? endDate : endDateOfLeave;

                    long days = ChronoUnit.DAYS.between(effectiveStart, effectiveEnd);

                    return (int) days + 1;
                })
                .sum();
    }
}
