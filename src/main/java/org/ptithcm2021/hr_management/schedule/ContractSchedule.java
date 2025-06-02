package org.ptithcm2021.hr_management.schedule;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.ptithcm2021.hr_management.repository.ContractRepository;
import org.ptithcm2021.hr_management.service.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractSchedule {
    private final ContractRepository contractRepository;
    private final NotificationService notificationService;
    private final TaskScheduler taskScheduler;

    @Scheduled(cron = "0 0 0 * * *")
    public void  notiContractStatus(){
        List<Contract> contracts = contractRepository.findAllByContractStatusEnum(ContractStatusEnum.ACTIVE);

        contracts.forEach(contract ->{
            if (contract.getEndDate() != null) {

                long numDayLeft = ChronoUnit.DAYS.between(LocalDate.now(), contract.getEndDate());
                if (numDayLeft == 30) {

//                    contract.setContractStatusEnum(ContractStatusEnum.EXPIRING_SOON);
//                    contractRepository.save(contract);

                    try {
                        notificationService.createNotification(
                                notificationContractExpirySoon(contract.getUser().getId(), contract.getEndDate()));
                    } catch (FirebaseMessagingException e) {
                        throw new RuntimeException(e);
                    }
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

    @Scheduled(cron = "0 0 0 * * *")
    public void updateContractExpiry() {
        log.info("Updating contract expiry");
        List<Contract> contracts = contractRepository.findAllContractExpiry(LocalDate.now());
        contracts.forEach(contract -> {
            contract.setContractStatusEnum(ContractStatusEnum.EXPIRED);
        });
        contractRepository.saveAll(contracts);
    }

    public void scheduleContractStatusUpdate(int contractId, LocalDateTime runAt, ContractStatusEnum contractStatusEnum ) {
        Runnable task = () -> {
            contractRepository.findById(contractId).ifPresent(contract -> {
                contract.setContractStatusEnum(contractStatusEnum);

                if (contractStatusEnum == ContractStatusEnum.ACTIVE) {
                    contract.getUser().setStatus(UserStatusEnum.ACTIVE);
                }

                contractRepository.save(contract);

                log.info("Contract " + contractId + " đã được cập nhật!");
            });
        };

        Date runDate = Date.from(runAt.atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(task, runDate);
    }


    private NotificationRequest notificationContractExpirySoon(long userId, LocalDate endDate){
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
