package org.ptithcm2021.hr_management.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.ptithcm2021.hr_management.repository.ContractRepository;
import org.ptithcm2021.hr_management.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractSchedule {
    private final ContractRepository contractRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * *")
    public void  notiContractStatus(){
        List<Contract> contracts = contractRepository.findContractByContractStatusEnum(ContractStatusEnum.PENDING);

        contracts.forEach(contract ->{
            if (contract.getEndDate() != null) {

                long numDayLeft = (contract.getEndDate().getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
                if (numDayLeft == 30) {

//                    contract.setContractStatusEnum(ContractStatusEnum.EXPIRING_SOON);
//                    contractRepository.save(contract);

                    notificationService.createNotification(
                            notificationContractExpirySoon(contract.getUser().getId(), contract.getEndDate()));
                }
            }
        });
    }

    /**
     * Cập nhật trạng thái hợp đồng vào ngày đầu tiên của mỗi tháng lúc 00:01
     * Chuyển hợp đồng từ "đã ký chờ hiệu lực" sang "đang có hiệu lực"
     * Áp dụng cho các hợp đồng được ký lại do thăng chức hoặc gia hạn
     */
    @Scheduled(cron = "0 1 0 1 * ?", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void updateContractStatus() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        log.info("Updating contract statuses on the first day of month: {}", firstDayOfMonth);

        try {
            // Lấy danh sách hợp đồng đã ký nhưng chưa có hiệu lực
            List<Contract> pendingContracts = contractRepository.findAllByContractStatusEnum(
                    ContractStatusEnum.SIGNED_PENDING_EFFECTIVE);

            if (pendingContracts.isEmpty()) {
                log.info("No pending contracts to activate");
                return;
            }

            // Cập nhật trạng thái của các hợp đồng này thành ACTIVE
            for (Contract contract : pendingContracts) {
                contract.setContractStatusEnum(ContractStatusEnum.ACTIVE);
                contractRepository.save(contract);
                log.info("Contract ID {} for user ID {} has been activated",
                        contract.getId(), contract.getUser().getId());
            }

            log.info("Successfully updated {} contracts to ACTIVE status", pendingContracts.size());
        } catch (Exception e) {
            log.error("Error updating contract statuses: {}", e.getMessage(), e);
        }
    }

    private NotificationRequest notificationContractExpirySoon(long userId, Date endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");  // Định dạng ngày tháng (có thể thay đổi theo yêu cầu)
        String formattedEndDate = sdf.format(endDate);  // Chuyển đổi endDate thành chuỗi với định dạng mong muốn

        String content = String.format("Chúng tôi xin thông báo rằng hợp đồng lao động của bạn sẽ hết hạn vào ngày %s. Xin vui lòng xem xét và thực hiện các bước tiếp theo theo quy định để gia hạn hợp đồng nếu cần.", formattedEndDate);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle("Your contract is about to expire.");
        notificationRequest.setContent(content);
        notificationRequest.setReceiverIds(Collections.singletonList(userId));

        return notificationRequest;
    }
}
