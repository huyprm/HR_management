package org.ptithcm2021.hr_management.schedule;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.Contract;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.ContractRepository;
import org.ptithcm2021.hr_management.repository.LeaveApplicationRepository;
import org.ptithcm2021.hr_management.repository.LeaveBalanceRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.ptithcm2021.hr_management.service.ContractService;
import org.ptithcm2021.hr_management.util.LeaveApplicationUtil;
import org.ptithcm2021.hr_management.util.LeaveBalanceUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class LeaveBalanceSchedule {
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;
    private final LeaveApplicationUtil leaveApplicationUtil;

    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Ho_Chi_Minh")
    public void rolloverLeaveBalances() {
        YearMonth now = YearMonth.now().minusMonths(1);
        int year = now.getYear();
        int month = now.getMonthValue();

        List<User> contracts = userRepository.findActiveOrRecentlyEndedContractUsers(now.atDay(1), now.atEndOfMonth(), ContractStatusEnum.ACTIVE);

        contracts.forEach(user -> {
            processMonthlyLeaveBalance(user, now.atDay(1), now.atEndOfMonth(), month, year);
        });
    }

    private void processMonthlyLeaveBalance(User user, LocalDate startDate, LocalDate endDate, int month, int year) {
        // Tính tổng ngày nghỉ trong tháng đó (truy vấn đơn nghỉ đã approved, giao với ngày từ 1->endOfMonth)
        double used = leaveApplicationUtil.calculateLeveDays(user.getId(), startDate, endDate);

        // Cộng thêm ngày phép mới
        double accrued = 1;

        if (user.getSeniorityAllowanceRule()!=null){
            accrued = (double) (12 + user.getSeniorityAllowanceRule().getSeniorityLeaveDay()) /12;
        }
        // Lấy carriedOver từ tháng trước
        LeaveBalance previous = leaveBalanceRepository.findByUserIdAndYearAndMonth(user.getId(), year, month)
                .orElse(null);

        double carried = previous != null ? previous.getRemainingLeaveDay() : 0;

        // Tính lại remaining
        double remaining = accrued + carried - used;
        if (remaining < 0) {
            used = used - Math.abs(remaining);
            remaining = 0;
        }else if ( remaining > 36) remaining = 0;

        // Lưu lại bảng phép
        LeaveBalance lb = LeaveBalance.builder()
                .user(user)
                .usedLeaveDay(used)
                .carriedOverDay(carried)
                .remainingLeaveDay(remaining)
                .totalLeaveDay(accrued)
                .month(month)
                .year(year)
                .build();

        leaveBalanceRepository.save(lb);
    }

}
