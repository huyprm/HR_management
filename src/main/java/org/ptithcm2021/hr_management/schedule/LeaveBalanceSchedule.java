package org.ptithcm2021.hr_management.schedule;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.Contract;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.repository.LeaveBalanceRepository;
import org.ptithcm2021.hr_management.service.ContractService;
import org.ptithcm2021.hr_management.util.LeaveBalanceUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
    private final ContractService contractService;

    @Scheduled(cron = "0 43 22 * * *", zone = "Asia/Ho_Chi_Minh")
    public void rolloverLeaveBalances() {
        YearMonth now = YearMonth.now().minusMonths(1);
        int year = now.getYear();
        int month = now.getMonthValue();
        int lastYear = Year.now().getValue() - 1;

        List<Contract> contracts = contractService.getAllContractIsActive();

        Map<Long, Contract> contractMap = contracts.stream()
                .collect(Collectors.toMap(contract -> contract.getUser().getId(), contract -> contract));

        List<LeaveBalance> leaveBalances = leaveBalanceRepository.findAllByYearAndMonth(year, month)
                .orElse(null);

        if (leaveBalances == null) {

        }
        leaveBalances.forEach(oldBalance -> {

            Contract contract = contractMap.get(oldBalance.getUser().getId());

            if (contract == null) return;

            int carried = oldBalance.getRemainingLeaveDay() > 36 ? 0 : oldBalance.getRemainingLeaveDay();

            int leaveDay = LeaveBalanceUtil.calculateLeaveDaysInYear(new Date(), contract.getEndDate());

            LeaveBalance newBalance = new LeaveBalance();
            newBalance.setUsedLeaveDay(0);
            newBalance.setYear(Year.now().getValue());
            newBalance.setTotalLeaveDay(leaveDay);
            newBalance.setCarriedOverDay(carried);
            newBalance.setUser(oldBalance.getUser());

            leaveBalanceRepository.save(newBalance);
        });
    }

//    void processMonthlyLeaveBalance(User user, YearMonth ym) {
//        // 1. Tính tổng ngày nghỉ trong tháng đó (truy vấn đơn nghỉ đã approved, giao với ngày từ 1->endOfMonth)
//        int used = leaveApplicationRepository.countLeaveDaysInMonth(user, ym);
//
//        // 2. Cộng thêm ngày phép mới (ví dụ 1 ngày/tháng nếu làm đủ)
//        int accrued = 1;
//
//        // 3. Lấy carriedOver từ tháng trước
//        LeaveBalance previous = leaveBalanceRepository.findByUserAndYearMonth(user, ym.minusMonths(1));
//        int carried = previous != null ? previous.getRemainingDays() : 0;
//
//        // 4. Tính lại remaining
//        int remaining = accrued + carried - used;
//        if (remaining < 0) remaining = 0;
//
//        // 5. Lưu lại bảng phép
//        LeaveBalance lb = new LeaveBalance();
//        lb.setUser(user);
//        lb.setYearMonth(ym);
//        lb.setAccruedDays(accrued);
//        lb.setUsedDays(used);
//        lb.setCarriedOver(carried);
//        lb.setRemainingDays(remaining);
//        lb.setCalculatedAt(LocalDate.now());
//
//        leaveBalanceRepository.save(lb);
//    }

}
