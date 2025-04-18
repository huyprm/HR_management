package org.ptithcm2021.hr_management.service.impl;

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
import org.ptithcm2021.hr_management.repository.LeaveDayRepository;
import org.ptithcm2021.hr_management.service.LeaveBalanceService;
import org.ptithcm2021.hr_management.service.UserService;
import org.ptithcm2021.hr_management.util.LeaveBalanceUtil;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements LeaveBalanceService {
    private final LeaveBalanceMapper leaveBalanceMapper;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final UserService userService;
    private final LeaveDayRepository leaveDayRepository;

    @Override
    public void createLeaveBalance(LeaveBalanceRequest leaveBalanceRequest) {
        User user = userService.getUserToUser(leaveBalanceRequest.getUserId());

        LeaveBalance leaveBalance = leaveBalanceMapper.toLeaveBalance(leaveBalanceRequest);
        leaveBalance.setUser(user);
        
        // Nếu không chỉ định tháng, sử dụng tháng hiện tại
        if (leaveBalance.getMonth() == 0) {
            leaveBalance.setMonth(LocalDate.now().getMonthValue());
        }

        leaveBalanceRepository.save(leaveBalance);
    }

    // Phương thức mới theo tháng
    @Override
    public void dayOff(int year, int month, LeaveApplication leaveApplication) {
        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByUserIdAndYearAndMonth(leaveApplication.getUser().getId(), year, month)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_BALANCE_NOT_FOUND));

        // Tính số ngày nghỉ
        LocalDate endDate = leaveApplication.getEndDate();

        LocalDate startDate = leaveApplication.getStartDate();

        // Tạo đối tượng YearMonth
        YearMonth yearMonth = YearMonth.of(year, month);

        // Lấy ngày cuối cùng của tháng
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        if (startDate == null || endDate == null) {
            throw new AppException(ErrorCode.INVALID_LEAVE_APPLICATION);
        }

        LocalDate toDate = lastDayOfMonth.isAfter(endDate) ? endDate : lastDayOfMonth;

        int workActualDays = LeaveBalanceUtil.calculateActualWorkingDays(startDate, toDate, leaveDayRepository);

        if (workActualDays < 0) {
            throw new AppException(ErrorCode.INVALID_LEAVE_APPLICATION);
        }
        
        // Kiểm tra xem loại nghỉ phép có ảnh hưởng đến số ngày nghỉ phép không
        // Nếu là nghỉ BHXH thì affectLeaveBalance = false
        if (leaveApplication.getLeaveType() != null && leaveApplication.getLeaveType().isAffectLeaveBalance()) {
            leaveBalance.setUsedLeaveDay(leaveBalance.getUsedLeaveDay() + workActualDays);
            leaveBalanceRepository.save(leaveBalance);
        }
    }

    @Override
    public void updateLeaveBalance(LeaveBalance leaveBalance) {
        leaveBalanceRepository.save(leaveBalance);
    }

    @Override
    public LeaveBalance getLeaveBalanceToLeaveBalance(long userId) {
        // Trả về thông tin cho tháng và năm hiện tại
        int currentYear = Year.now().getValue();
        int currentMonth = LocalDate.now().getMonthValue();
        
        return leaveBalanceRepository.findByUserIdAndYearAndMonth(userId, currentYear, currentMonth)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_BALANCE_NOT_FOUND));
    }
    
    @Override
    public List<LeaveBalance> getAllLeaveBalancesByYear(long userId, int year) {
        return leaveBalanceRepository.findAllByUserIdAndYear(userId, year);
    }

    @Override
    public LeaveBalanceResponse getLeaveBalance(long userId) {
        int currentYear = Year.now().getValue();
        int currentMonth = LocalDate.now().getMonthValue();
        
        // Tìm kiếm theo tháng và năm hiện tại
        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByUserIdAndYearAndMonth(userId, currentYear, currentMonth)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_BALANCE_NOT_FOUND));

        return leaveBalanceMapper.toLeaveBalanceResponse(leaveBalance);
    }
    
    @Override
    public LeaveBalanceResponse getLeaveBalanceByMonth(long userId, int year, int month) {
        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByUserIdAndYearAndMonth(userId, year, month)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_BALANCE_NOT_FOUND));

        return leaveBalanceMapper.toLeaveBalanceResponse(leaveBalance);
    }
    

}
