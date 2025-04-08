package org.ptithcm2021.hr_management.schedule;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.ptithcm2021.hr_management.repository.ContractRepository;
import org.ptithcm2021.hr_management.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractSchedule {
    private final ContractRepository contractRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateContractStatus(){
        List<Contract> contracts = contractRepository.findContractByContractStatusEnum(ContractStatusEnum.PENDING);

        contracts.forEach(contract ->{
            if (contract.getEndDate() != null) {

                long numDayLeft = (contract.getEndDate().getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
                if (numDayLeft == 30) {

                    contract.setContractStatusEnum(ContractStatusEnum.EXPIRING_SOON);
                    contractRepository.save(contract);

                    notificationService.createNotification(
                            notificationContractExpirySoon(contract.getUser().getId(), contract.getEndDate()));
                }
            }
        });
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
