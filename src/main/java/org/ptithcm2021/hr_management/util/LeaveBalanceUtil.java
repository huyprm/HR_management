package org.ptithcm2021.hr_management.util;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class LeaveBalanceUtil {
    public static int calculateLeaveDaysInYear(Date startDate, Date endDate) {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = (endDate == null)
                ? LocalDate.of(Year.now().getValue(), 12, 31)  // nếu chưa nghỉ thì dùng cuối năm
                : endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Nếu làm từ năm trước, chỉ lấy phần từ 1/1 năm nay
        LocalDate yearStart = LocalDate.of(Year.now().getValue(), 1, 1);
        if (start.isBefore(yearStart)) {
            start = yearStart;
        }

        // Nếu kết thúc sau năm nay, chỉ lấy phần đến 31/12
        LocalDate yearEnd = LocalDate.of(Year.now().getValue(), 12, 31);
        if (end.isAfter(yearEnd)) {
            end = yearEnd;
        }

        long days = ChronoUnit.DAYS.between(start, end.plusDays(1));
        double leaveDays = 12.0 * days / Year.now().length();
        return (int) Math.round(leaveDays); // tối đa 12 ngày nghỉ
    }
}
