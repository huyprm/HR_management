package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LeaveBalanceRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveBalanceResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.LeaveBalanceMapper;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.LeaveApplicationRepository;
import org.ptithcm2021.hr_management.repository.LeaveBalanceRepository;
import org.ptithcm2021.hr_management.service.LeaveBalanceService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements LeaveBalanceService {
    private final LeaveBalanceMapper leaveBalanceMapper;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final UserService userService;

    @Override
    public void createLeaveBalance(LeaveBalanceRequest leaveBalanceRequest) {
        User user = userService.getUserToUser(leaveBalanceRequest.getUserId());

        LeaveBalance leaveBalance = leaveBalanceMapper.toLeaveBalance(leaveBalanceRequest);
        leaveBalance.setUser(user);

        leaveBalanceRepository.save(leaveBalance);
    }

    @Override
    public void dayOff(int year,LeaveApplication leaveApplication) {
        LeaveBalance leaveBalance = leaveBalanceRepository.findByUserIdAndYear(leaveApplication.getUser().getId(), year)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_BALANCE_NOT_FOUND));

        LocalDateTime startDate = leaveApplication.getStartDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime endDate = leaveApplication.getEndDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        long numDay = ChronoUnit.DAYS.between(startDate, endDate);
        leaveBalance.setUsedLeaveDay(leaveBalance.getUsedLeaveDay() + (int)numDay + 1);

        leaveBalanceRepository.save(leaveBalance);
    }

    @Override
    @Scheduled(cron = "0 0 0 1 1 *", zone = "Asia/Ho_Chi_Minh")
    public void rolloverLeaveBalances() {
        int lastYear = Year.now().getValue() - 1;

        List<LeaveBalance> leaveBalances = leaveBalanceRepository.findAllByYear(lastYear)
                .orElseThrow(()->new AppException(ErrorCode.LEAVE_BALANCE_NOT_FOUND));

        leaveBalances.forEach(oldBalance -> {
            int carried = oldBalance.getRemainingLeave() > 36 ? 0 : oldBalance.getRemainingLeave();

            //contractService.getCurrentContractOfUser(long userId);
            LeaveBalance newBalance = new LeaveBalance();
            newBalance.setUsedLeaveDay(0);
            newBalance.setYear(Year.now().getValue());

            //ChronoUnit.MONTHS.between(now, contrac.getEndDate()) + 1 ? 12
            newBalance.setTotalLeaveDay(oldBalance.getTotalLeaveDay());
            newBalance.setCarriedOverDay(carried);

            leaveBalanceRepository.save(newBalance);
        });
    }

    @Override
    public LeaveBalanceResponse getLeaveBalance(long userId) {
        LeaveBalance leaveBalance = leaveBalanceRepository.findByUserIdAndYear(userId, Year.now().getValue())
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_BALANCE_NOT_FOUND));

        return leaveBalanceMapper.toLeaveBalanceResponse(leaveBalance);
    }


}
