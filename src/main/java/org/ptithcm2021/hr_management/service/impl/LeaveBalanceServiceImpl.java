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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements LeaveBalanceService {
    private final LeaveBalanceMapper leaveBalanceMapper;
    private final LeaveBalanceRepository leaveBalanceRepository;

    @Override
    public LeaveBalanceResponse getLeaveBalanceToLeaveBalance(long userId) {
        // Trả về thông tin cho tháng và năm hiện tại
        int currentYear = Year.now().getValue();
        int currentMonth = LocalDate.now().getMonthValue();
        
        LeaveBalance leaveBalance = leaveBalanceRepository.findByUserIdAndYearAndMonth(userId, currentYear, currentMonth)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_BALANCE_NOT_FOUND));

        return leaveBalanceMapper.toLeaveBalanceResponse(leaveBalance);
    }

    @Override
    public List<LeaveBalanceResponse> getAllLeaveBalancesByYear(long userId, int year) {
        return leaveBalanceRepository.findAllByUserIdAndYear(userId, year)
                .stream().map(leaveBalanceMapper::toLeaveBalanceResponse).collect(Collectors.toList());
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
