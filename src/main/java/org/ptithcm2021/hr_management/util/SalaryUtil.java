package org.ptithcm2021.hr_management.util;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

@Slf4j
public final class SalaryUtil {
    /**
     * Tính số ngày làm việc trong tháng (không bao gồm thứ 7 và chủ nhật)
     * @param yearMonth Tháng cần tính số ngày làm việc
     * @return Số ngày làm việc trong tháng
     */
    public static int calculateWorkingDaysInMonth(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        int workingDays = 0;

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        log.info("Working days in {}: {}", yearMonth, workingDays);
        return workingDays;
    }
}
