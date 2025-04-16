package org.ptithcm2021.hr_management.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.ptithcm2021.hr_management.repository.ContractRepository;
import org.ptithcm2021.hr_management.service.SalaryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalarySchedule {
    private final SalaryService salaryService;
    private final ContractRepository contractRepository;
    
    /**
     * Tự động tạo lương vào ngày cuối cùng của mỗi tháng lúc 23:00
     * Pattern: giây phút giờ ngày tháng thứ
     */
    @Scheduled(cron = "0 0 23 L * ?", zone = "Asia/Ho_Chi_Minh")
    public void generateMonthlySalaries() {
        // Lấy tháng hiện tại để tính lương
        YearMonth currentMonth = YearMonth.now();
        log.info("Starting automatic salary generation for month: {}", currentMonth);
        
        try {
            salaryService.generateMonthlySalaries(currentMonth);
            log.info("Successfully generated salaries for month: {}", currentMonth);
        } catch (Exception e) {
            log.error("Error generating salaries for month {}: {}", currentMonth, e.getMessage(), e);
        }
    }
    

}