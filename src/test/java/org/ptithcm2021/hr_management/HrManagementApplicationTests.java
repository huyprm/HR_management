package org.ptithcm2021.hr_management;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.hr_management.util.LeaveBalanceUtil;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions.toDate;

@ExtendWith(MockitoExtension.class)
class HrManagementApplicationTests {
    private final LeaveBalanceUtil util = new LeaveBalanceUtil();
    private Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    @Test
    void contextLoads() {
        Date start = toDate(LocalDate.of(2025, 3, 29));
        Date end = toDate(LocalDate.of(2025, 12,31));
        int leaveDays = util.calculateLeaveDaysInYear(start, end);
        assertEquals(5, leaveDays);
    }
    @Test
    public void testStartAndEndInSameYear() {
        Date start = toDate(LocalDate.of(2025, 1, 1));
        Date end = toDate(LocalDate.of(2025, 3, 5));
        int leaveDays = util.calculateLeaveDaysInYear(start, end);
        assertEquals(5, leaveDays); // Từ giữa tháng 1 đến giữa tháng 6 => 5 tháng tính đủ
    }
}
