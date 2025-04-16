package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.SalaryRequest;
import org.ptithcm2021.hr_management.dto.response.SalaryResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.SalaryMapper;
import org.ptithcm2021.hr_management.model.Contract;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.model.Salary;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.ContractRepository;
import org.ptithcm2021.hr_management.repository.LeaveBalanceRepository;
import org.ptithcm2021.hr_management.repository.SalaryRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.ptithcm2021.hr_management.schedule.SalarySchedule;
import org.ptithcm2021.hr_management.service.SalaryService;
import org.ptithcm2021.hr_management.service.SeniorityAllowanceRuleService;
import org.ptithcm2021.hr_management.service.UserService;
import org.ptithcm2021.hr_management.util.SalaryUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {
    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;
    private final SalaryMapper salaryMapper;
    private final UserService userService;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final SeniorityAllowanceRuleService seniorityAllowanceRuleService;

    @Override
    public SalaryResponse createSalary(SalaryRequest salaryRequest) {
        User user = userService.getUserToUser(salaryRequest.getUserId());
        
        if (salaryRepository.existsByUserIdAndSalaryMonth(user.getId(), salaryRequest.getSalaryMonth())) {
            throw new AppException(ErrorCode.SALARY_ALREADY_EXISTS);
        }
        
        Contract contract = contractRepository.findById(salaryRequest.getContractId())
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
                
        // Validate if the contract belongs to the user
        if (contract.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.INVALID_CONTRACT_USER);
        }

        Salary salary = salaryMapper.toSalary(salaryRequest);
        salary.setUser(user);
        salary.setContract(contract);
        
        // Set default values if not provided
        salary.setBaseSalary(contract.getBasicSalary());
        

        // Calculate seniority allowance
        double seniorityAllowancePercentage = 0;
        if(user.getSeniorityAllowanceRule() != null){
            seniorityAllowancePercentage = user.getSeniorityAllowanceRule().getSeniorityPercentage();
        }

        salary.setTotalAllowance(calculateTotalAllowance(contract, seniorityAllowancePercentage));

        // Calculate unpaid leave deduction
        salary.setUnpaidLeaveDeduction(calculateUnpaidLeaveDeduction(user, salaryRequest.getSalaryMonth(), contract));

        // Set payment date to current date
        salary.setPaymentDate(new Date());
        
        return salaryMapper.toSalaryResponse(salaryRepository.save(salary));
    }

    @Override
    public void deleteSalary(int id) {
        if (!salaryRepository.existsById(id)) {
            throw new AppException(ErrorCode.SALARY_NOT_FOUND);
        }
        
        try {
            salaryRepository.deleteById(id);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }

    @Override
    public SalaryResponse getSalary(int id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SALARY_NOT_FOUND));
        
        return salaryMapper.toSalaryResponse(salary);
    }

    @Override
    public List<SalaryResponse> getSalariesByUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        
        List<Salary> salaries = salaryRepository.findAllByUserId(userId);
        
        return salaries.stream()
                .map(salaryMapper::toSalaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryResponse> getSalariesByMonth(YearMonth yearMonth) {
        List<Salary> salaries = salaryRepository.findAllBySalaryMonth(yearMonth);
        
        return salaries.stream()
                .map(salaryMapper::toSalaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void generateMonthlySalaries(YearMonth yearMonth) {
        // Get all active users with contracts - active users here are PENDING
        List<User> activeUsers = userRepository.findAllActiveUsers();
        
        // Xác định ngày đầu tiên và ngày cuối cùng của tháng
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        
        // Tính tổng số ngày làm việc trong tháng (không bao gồm thứ 7 và chủ nhật)
        int workingDaysInMonth = SalaryUtil.calculateWorkingDaysInMonth(yearMonth);
        
        for (User user : activeUsers) {
            // Kiểm tra trạng thái người dùng - chỉ xử lý người dùng PENDING (đang hoạt động)
            if (user.getStatus() != UserStatusEnum.PENDING) {
                continue;
            }
            
            // Skip if salary for this month already exists
            if (salaryRepository.existsByUserIdAndSalaryMonth(user.getId(), yearMonth)) {
                continue;
            }
            
            // Get current contract for user - chỉ lấy hợp đồng ACTIVE
            Optional<Contract> activeContractOpt = contractRepository.findContractByUserIdAndContractStatusEnum(
                    user.getId(), ContractStatusEnum.ACTIVE);
            
            if (activeContractOpt.isEmpty()) {
                continue; // Skip users without active contract
            }
            
            Contract contract = activeContractOpt.get();
            
            // Chuyển đổi Date thành LocalDate
            LocalDate contractStartDate = new java.sql.Date(contract.getStartDate().getTime()).toLocalDate();
            LocalDate contractEndDate = contract.getEndDate() != null ? 
                    new java.sql.Date(contract.getEndDate().getTime()).toLocalDate() : null;
            
            // Kiểm tra xem hợp đồng có hiệu lực trong tháng này không
            if (contractEndDate != null && contractEndDate.isBefore(firstDayOfMonth)) {
                continue; // Hợp đồng đã kết thúc trước tháng này
            }
            
            if (contractStartDate.isAfter(lastDayOfMonth)) {
                continue; // Hợp đồng bắt đầu sau tháng này
            }
            
            // Xác định thời gian làm việc thực tế trong tháng
            LocalDate actualStartDate = contractStartDate.isAfter(firstDayOfMonth) ? 
                    contractStartDate : firstDayOfMonth;
            LocalDate actualEndDate = (contractEndDate != null && contractEndDate.isBefore(lastDayOfMonth)) ? 
                    contractEndDate : lastDayOfMonth;
            
            // Tính số ngày làm việc thực tế trong khoảng thời gian hợp đồng có hiệu lực (loại trừ thứ 7 và chủ nhật)
            int actualWorkingDays = calculateActualWorkingDays(actualStartDate, actualEndDate);
            
            // Create salary
            Salary salary = new Salary();
            salary.setUser(user);
            salary.setContract(contract);
            salary.setSalaryMonth(yearMonth);
            
            // Tính lương cơ bản theo tỷ lệ ngày làm việc thực tế so với tổng số ngày làm việc trong tháng
            double fullMonthSalary = contract.getBasicSalary();
            double actualSalary = fullMonthSalary * ((double) actualWorkingDays / workingDaysInMonth);
            salary.setBaseSalary(actualSalary);

            // Calculate seniority allowance
            double seniorityAllowancePercentage = 0;
            if(user.getSeniorityAllowanceRule() != null){
                seniorityAllowancePercentage = user.getSeniorityAllowanceRule().getSeniorityPercentage();
            }

            // Tính phụ cấp thâm niên theo tỷ lệ ngày làm việc thực tế
            double allowance = calculateTotalAllowance(contract, seniorityAllowancePercentage) *
                    ((double) actualWorkingDays / workingDaysInMonth);
            salary.setTotalAllowance(allowance);
            
            // Calculate unpaid leave deduction
            salary.setUnpaidLeaveDeduction(calculateUnpaidLeaveDeduction(user, yearMonth, contract));
            
            // Set payment date (typically last day of month or a specific day)
            LocalDate paymentDate = yearMonth.atEndOfMonth();
            salary.setPaymentDate(java.sql.Date.valueOf(paymentDate));
            
            salaryRepository.save(salary);
        }
    }
    
    /**
     * Tính số ngày làm việc thực tế trong khoảng thời gian (không bao gồm thứ 7 và chủ nhật)
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Số ngày làm việc thực tế
     */
    private int calculateActualWorkingDays(LocalDate startDate, LocalDate endDate) {
        int workingDays = 0;
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek() != java.time.DayOfWeek.SATURDAY && 
                currentDate.getDayOfWeek() != java.time.DayOfWeek.SUNDAY) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return workingDays;
    }

    @Override
    public List<SalaryResponse> getAllSalaries() {
        List<Salary> salaries = salaryRepository.findAll();
        
        return salaries.stream()
                .map(salaryMapper::toSalaryResponse)
                .collect(Collectors.toList());
    }
    
    private double calculateTotalAllowance(Contract contract, double seniorityAllowancePercentage) {
        double baseSalary = contract.getBasicSalary();
        
        return baseSalary * seniorityAllowancePercentage;
    }
    
    private double calculateUnpaidLeaveDeduction(User user, YearMonth yearMonth, Contract contract) {
        // Get the year for this salary month
        int year = yearMonth.getYear();
        
        try {
            // Get leave balance for the user and year
            Optional<LeaveBalance> leaveBalanceOpt = leaveBalanceRepository.findByUserIdAndYear(user.getId(), year);
            
            if (leaveBalanceOpt.isPresent()) {
                LeaveBalance leaveBalance = leaveBalanceOpt.get();
                int totalLeaveDay = leaveBalance.getTotalLeaveDay() + leaveBalance.getCarriedOverDay();
                int usedLeaveDay = leaveBalance.getUsedLeaveDay();
                
                // If used leave days exceed total allowed, calculate deduction
                if (usedLeaveDay > totalLeaveDay) {
                    int unpaidLeaveDays = usedLeaveDay - totalLeaveDay;

                    // Sử dụng số ngày làm việc trong tháng
                    int workingDaysInMonth = SalaryUtil.calculateWorkingDaysInMonth(yearMonth);
                    double dailySalary = contract.getBasicSalary() / workingDaysInMonth;
                    return dailySalary * unpaidLeaveDays;
                }
            }
            
            return 0.0; // No deduction if no unpaid leave or leave balance not found
        } catch (Exception e) {
            return 0.0; // Default to no deduction if error occurs
        }
    }

}