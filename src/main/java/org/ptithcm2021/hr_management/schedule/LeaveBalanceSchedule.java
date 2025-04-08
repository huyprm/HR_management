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
        int lastYear = Year.now().getValue() - 1;

        List<Contract> contracts = contractService.getAllContractIsPending();

        Map<Long, Contract> contractMap = contracts.stream()
                .collect(Collectors.toMap(contract -> contract.getUser().getId(), contract -> contract));

        List<LeaveBalance> leaveBalances = leaveBalanceRepository.findAllByYear(lastYear)
                .orElseThrow(()->new AppException(ErrorCode.LEAVE_BALANCE_NOT_FOUND));

        leaveBalances.forEach(oldBalance -> {

            Contract contract = contractMap.get(oldBalance.getUser().getId());

            if (contract == null) return;

            int carried = oldBalance.getRemainingLeave() > 36 ? 0 : oldBalance.getRemainingLeave();

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
}
