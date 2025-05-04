package org.ptithcm2021.hr_management.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.ptithcm2021.hr_management.model.*;
import org.ptithcm2021.hr_management.repository.DecisionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeniorityAllowanceSchedule {
    private final DecisionRepository decisionRepository;

    @Scheduled(cron = "0 0 0 1 * *")
    public void processPendingSeniorityAllowanceChanges() {
        try{
            Date currentDate = new Date();

            // Tìm các quyết định tăng lương chưa được xử lý và đến hạn áp dụng
            List<Decision> pendingDecisions = decisionRepository.findByTypeAndProcessedFalseAndEffectiveDateLessThanEqual(
                    DecisionEnum.SENIORITY_ALLOWANCE, currentDate);

            // Xử lý từng quyết định
            for (Decision decision : pendingDecisions) {
                try {
                    // Lấy thông tin từ quyết định
                    SeniorityAllowanceRule allowanceRule = decision.getSeniorityAllowanceRule();

                    if (allowanceRule != null) {
                        // Lấy user
                        User user = decision.getUser();

                        user.setSeniorityAllowanceRule(allowanceRule);

                        // Đánh dấu quyết định đã được xử lý
                        decision.setProcessed(true);
                        decisionRepository.save(decision);

                        log.info("Successfully processed seniority allowance change for user: {}",
                                decision.getUser().getFullName());
                    } else {
                        log.warn("Decision ID {} has no associated salary promotion", decision.getId());
                    }
                } catch (Exception e) {
                    log.error("Error processing salary change decision ID {}: {}", decision.getId(), e.getMessage(), e);
                }
            }

            log.info("Completed processing pending salary changes");
        }
        catch(Exception e){
            log.error("Error in pending salary changes processing job: {}", e.getMessage(), e);
        }
    }
}
