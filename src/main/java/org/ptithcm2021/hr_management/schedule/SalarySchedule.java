package org.ptithcm2021.hr_management.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.ptithcm2021.hr_management.model.Decision;
import org.ptithcm2021.hr_management.model.SalaryPromotion;
import org.ptithcm2021.hr_management.repository.ContractRepository;
import org.ptithcm2021.hr_management.repository.DecisionRepository;
import org.ptithcm2021.hr_management.service.ContractService;

import org.ptithcm2021.hr_management.service.SalaryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalarySchedule {
    private final SalaryService salaryService;
    private final ContractService contractService;
    private final DecisionRepository decisionRepository;

    
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
    
    /**
     * Tự động xử lý các quyết định tăng lương vào 00:01 ngày đầu tiên của mỗi tháng
     * Pattern: giây phút giờ ngày tháng thứ
     */
    @Scheduled(cron = "0 1 0 1 * ?", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void processPendingSalaryChanges() {
        log.info("Starting processing pending salary changes job");
        
        try {
            // Lấy ngày hiện tại
            Date currentDate = new Date();
            
            // Tìm các quyết định tăng lương chưa được xử lý và đến hạn áp dụng
            List<Decision> pendingDecisions = decisionRepository.findByTypeAndProcessedFalseAndEffectiveDateLessThanEqual(
                    DecisionEnum.INCREASE_SALARY, currentDate);
            
            log.info("Found {} pending salary change decisions to process", pendingDecisions.size());
            
            // Xử lý từng quyết định
            for (Decision decision : pendingDecisions) {
                try {
                    // Lấy thông tin từ quyết định
                    SalaryPromotion promotion = decision.getSalaryPromotion();
                    
                    if (promotion != null) {
                        // Lấy hợp đồng hiện tại của người dùng
                        Contract contract = contractService.getContractCurrentOfUser(decision.getUser().getId());
                        
                        // Cập nhật hợp đồng với JobGrade và lương mới
                        String newJobGradeId = promotion.getRequestJobGrade().getId();
                        
                        // Tính toán mức lương mới dựa trên hệ số nếu cần
                        double oldCoefficient = promotion.getCurrentJobGrade().getCoefficient();
                        double newCoefficient = promotion.getRequestJobGrade().getCoefficient();
                        double currentBasicSalary = contract.getBasicSalary();
                        double newBasicSalary = (currentBasicSalary / oldCoefficient) * newCoefficient;
                        
                        // Áp dụng thay đổi vào hợp đồng
                        contractService.updateContractWithPromotion(contract.getId(), newJobGradeId);
                        
                        // Đánh dấu quyết định đã được xử lý
                        decision.setProcessed(true);
                        decisionRepository.save(decision);
                        
                        log.info("Successfully processed salary change for user: {}, new job grade: {}, new salary: {}",
                                decision.getUser().getFullName(), newJobGradeId, newBasicSalary);
                    } else {
                        log.warn("Decision ID {} has no associated salary promotion", decision.getId());
                    }
                } catch (Exception e) {
                    log.error("Error processing salary change decision ID {}: {}", decision.getId(), e.getMessage(), e);
                    // Tiếp tục với quyết định tiếp theo nếu một quyết định thất bại
                }
            }
            
            log.info("Completed processing pending salary changes");
        } catch (Exception e) {
            log.error("Error in pending salary changes processing job: {}", e.getMessage(), e);
        }
    }

}