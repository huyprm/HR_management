package org.ptithcm2021.hr_management.service.impl;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.response.ContractExpireReportResponse;
import org.ptithcm2021.hr_management.dto.response.LeaveBalanceResponse;
import org.ptithcm2021.hr_management.dto.response.PayrollResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.UserSummaryMapper;
import org.ptithcm2021.hr_management.model.Contract;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.*;
import org.ptithcm2021.hr_management.service.LeaveApplicationService;
import org.ptithcm2021.hr_management.service.ReportService;
import org.ptithcm2021.hr_management.service.UserService;
import org.ptithcm2021.hr_management.util.LeaveBalanceUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final UserRepository userRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final ContractRepository contractRepository;
    private final UserSummaryMapper userSummaryMapper;
    private final LeaveDayRepository leaveDayRepository;

    @Override
    public List<PayrollResponse> getPayrollByMonth(LocalDate startDate, LocalDate endDate) {
        List<User> users = userRepository.findActiveOrRecentlyEndedContractUsers(startDate, endDate, ContractStatusEnum.ACTIVE);

        int workDays = LeaveBalanceUtil.calculateWorkingDays(startDate, endDate, leaveDayRepository);
        List<PayrollResponse> payrollResponses = new ArrayList<>();

        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        for (User user : users) {
            PayrollResponse payrollResponse = new PayrollResponse();

            payrollResponse.setFullName(user.getFullName());
            payrollResponse.setUserId(user.getId());
            payrollResponse.setSalary(user.getSalaryBasic());

            if (user.getSeniorityAllowanceRule()!= null){
                payrollResponse.setSeniority(user.getSeniorityAllowanceRule().getSeniorityPercentage());
            } else payrollResponse.setSeniority(0);

            payrollResponse.setWorkDays(workDays);


            Contract contract = contractRepository.findContractByUserIdAndContractStatusEnum(user.getId(), ContractStatusEnum.ACTIVE)
                    .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
            double unpaidLeave = 0;
            double paidLeave = 0;

            if(contract.getContractType().isPolicy()){
                ActualWorkingDays actualWorkingDays = calculateActualWorkingDays(user.getId(), startDate, endDate);

                unpaidLeave = actualWorkingDays.unpaidLeave;
                paidLeave = actualWorkingDays.paidLeave;
            }

            payrollResponse.setActualWorkDays(workDays - unpaidLeave -paidLeave);
            payrollResponse.setUnpaidLeaveDays(unpaidLeave);

            payrollResponses.add(payrollResponse);
        }

        return payrollResponses;
    }

    private ActualWorkingDays calculateActualWorkingDays(long userId, LocalDate startDate, LocalDate endDate) {
        double unpaidLeave = 0;
        double paidLeave = 0;

        var leaveList = leaveApplicationRepository.findApprovedLeavesByUserAndMonth(userId, startDate, endDate);

        for (var leaveApplication : leaveList) {
            LocalDate startDateOfLeave = leaveApplication.getStartDate();
            LocalDate endDateOfLeave = leaveApplication.getEndDate();

            LocalDate effectiveStart = startDateOfLeave.isBefore(startDate) ? startDate : startDateOfLeave;
            LocalDate effectiveEnd = endDateOfLeave.isAfter(endDate) ? endDate : endDateOfLeave;

            long days = ChronoUnit.DAYS.between(effectiveStart, effectiveEnd) + 1;

            if (leaveApplication.getLeaveType().isAffectLeaveBalance()) {
                paidLeave += days;
            } else {
                unpaidLeave += days;
            }
        }

        // Chắc chắn sử dụng đúng số phép
        LeaveBalance leaveBalance = leaveBalanceRepository.findByUserIdAndYearAndMonth(userId, endDate.getYear(), endDate.getMonthValue())
                .orElse(null);

        // Khi tính lương tháng đầu tiên chưa có tổng hợp từ tháng trước
        if (leaveBalance == null) {
            if(paidLeave > 1) {
                unpaidLeave = unpaidLeave + paidLeave -1;
                paidLeave = 1;

            }
        }else {
            if(paidLeave > leaveBalance.getRemainingLeaveDay()) {
                unpaidLeave = paidLeave - leaveBalance.getRemainingLeaveDay();
                paidLeave = leaveBalance.getRemainingLeaveDay();
            }
        }
        return new ActualWorkingDays(unpaidLeave, paidLeave);
    }

    @Override
    public List<ContractExpireReportResponse> getExpiringContracts(int days) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = today.plusDays(days);

        List<Contract> contracts = contractRepository.findActiveContractsExpiringBetween(today, deadline);

        AtomicInteger index = new AtomicInteger(1);

        return contracts.stream().map(c -> ContractExpireReportResponse.builder()
                .stt(index.getAndIncrement())
                .fullName(c.getUser().getFullName())
                .email(c.getUser().getEmail())
                .departmentName(c.getPosition().getDepartment() != null
                        ? c.getPosition().getDepartment().getName()
                        : "Chưa có")
                .positionName(c.getPosition().getName())
                .contractTypeName(c.getContractType().getName())
                .endDate(c.getEndDate())
                .remainingDays((int) ChronoUnit.DAYS.between(today, c.getEndDate())) // sửa chỗ này
                .contractStatus(c.getContractStatusEnum().name())
                .build()
        ).toList();
    }


    private record ActualWorkingDays(double unpaidLeave, double paidLeave) {}

}
