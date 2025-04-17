package org.ptithcm2021.hr_management.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.repository.LeaveDayRepository;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
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

    /**
     * Tính số ngày làm việc thực tế trong khoảng thời gian (không bao gồm thứ 7 và chủ nhật)
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Số ngày làm việc thực tế
     */
    public static int calculateActualWorkingDays(LocalDate startDate, LocalDate endDate, LeaveDayRepository leaveDayRepository) {
        int workingDays = 0;

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek() != java.time.DayOfWeek.SATURDAY &&
                    currentDate.getDayOfWeek() != java.time.DayOfWeek.SUNDAY) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        int numLeaveDay = leaveDayRepository.findAllByMonth(startDate, endDate).size();

        return workingDays - numLeaveDay;
    }

    /**
     * Tính số ngày làm việc trong tháng (không bao gồm thứ 7 và chủ nhật)
     * @param yearMonth Tháng cần tính số ngày làm việc
     * @return Số ngày làm việc trong tháng
     */
    public static int calculateWorkingDaysInMonth(YearMonth yearMonth, LeaveDayRepository leaveDayRepository) {
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
        int numLeaveDay = leaveDayRepository.findAllByMonth(startDate, endDate).size();

        return workingDays - numLeaveDay;
    }
}
