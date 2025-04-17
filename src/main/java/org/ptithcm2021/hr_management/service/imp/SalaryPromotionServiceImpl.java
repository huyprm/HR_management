package org.ptithcm2021.hr_management.service.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionRequest;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.SalaryPromotionResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.enums.WorkLogTypeEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.SalaryPromotionMapper;
import org.ptithcm2021.hr_management.model.SalaryPromotion;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.model.Decision;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.ptithcm2021.hr_management.model.WorkingHistory;
import org.ptithcm2021.hr_management.repository.SalaryPromotionRepository;
import org.ptithcm2021.hr_management.repository.JobGradeRepository;
import org.ptithcm2021.hr_management.repository.DecisionRepository;
import org.ptithcm2021.hr_management.repository.WorkLogRepository;
import org.ptithcm2021.hr_management.service.ContractService;
import org.ptithcm2021.hr_management.service.SalaryPromotionService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryPromotionServiceImpl implements SalaryPromotionService {
    private final SalaryPromotionRepository salaryPromotionRepository;
    private final SalaryPromotionMapper salaryPromotionMapper;
    private final UserService userService;
    private final ContractService contractService;
    private final DecisionRepository decisionRepository;
    private final WorkLogRepository workLogRepository;

    @Override
    public SalaryPromotionResponse createSalaryPromotion(SalaryPromotionRequest salaryPromotionRequest) {
        User user = userService.getUserToUser(salaryPromotionRequest.getUserId());

        SalaryPromotion salaryPromotion = salaryPromotionMapper.toSalaryPromotion(salaryPromotionRequest);
        salaryPromotion.setUser(user);

        return salaryPromotionMapper.toSalaryPromotionResponse(salaryPromotionRepository.save(salaryPromotion));
    }

    @Override
    public SalaryPromotionResponse getSalaryPromotionById(int id) {
        SalaryPromotion salaryPromotion = salaryPromotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND));

        return salaryPromotionMapper.toSalaryPromotionResponse(salaryPromotion);
    }

    @Override
    public SalaryPromotionResponse updateSalaryPromotion(int id, SalaryPromotionUpdateRequest updateRequest) {
        SalaryPromotion promotion = salaryPromotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND));

        if (promotion.getStatus() != FormStatusEnum.PENDING) {
            throw new AppException(ErrorCode.PROMOTION_ALREADY_PROCESSED);
        }

        User signer = userService.getUserToUser(updateRequest.getSignerId());
        promotion.setSigner(signer);
        promotion.setNote(updateRequest.getReason());

        promotion.setStatus(updateRequest.getFormStatus());

        if (updateRequest.getFormStatus() == FormStatusEnum.APPROVED) {
            applyApprovedSalaryPromotion(promotion);
        }

        promotion = salaryPromotionRepository.save(promotion);

        return salaryPromotionMapper.toSalaryPromotionResponse(salaryPromotionRepository.save(promotion));

    }

    @Override
    public void deleteSalaryPromotion(int id) {
        if(!salaryPromotionRepository.existsById(id)) {
            throw new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND);
        }
        try{
            salaryPromotionRepository.deleteById(id);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }

    @Override
    public List<SalaryPromotionResponse> getSalaryPromotionByStatusAndUserId(long userId, FormStatusEnum formStatus) {
        if(formStatus == null) {
            return salaryPromotionRepository.findAll().stream()
                    .map(salaryPromotionMapper::toSalaryPromotionResponse).collect(Collectors.toList());
        }

        return salaryPromotionRepository.findAllByStatusAndUserId(formStatus, userId)
                .stream().map(salaryPromotionMapper::toSalaryPromotionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryPromotionResponse> getSalaryPromotionByStatusAndSignId(long signer, FormStatusEnum formStatus) {
        return salaryPromotionRepository.findAllByStatusAndSignerId(formStatus, signer)
                .stream().map(salaryPromotionMapper::toSalaryPromotionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Áp dụng thay đổi khi đề xuất tăng lương được phê duyệt
     * Tạo một Decision với ngày hiệu lực vào đầu tháng sau
     */
    private void applyApprovedSalaryPromotion(SalaryPromotion promotion) {
        try {
            var contract = contractService.getContractCurrentOfUser(promotion.getUser().getId());

            double oldCoefficient = promotion.getCurrentJobGrade().getCoefficient();
            double newCoefficient = promotion.getRequestJobGrade().getCoefficient();
            double currentBasicSalary = contract.getBasicSalary();
            double newBasicSalary = (currentBasicSalary / oldCoefficient) * newCoefficient;

            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate firstDayOfNextMonth = today.withDayOfMonth(1).plusMonths(1);
            java.util.Date effectiveDate = java.sql.Date.valueOf(firstDayOfNextMonth);

            String decisionContent = "Quyết định tăng lương cho " + promotion.getUser().getFullName() 
                    + " từ cấp " + promotion.getCurrentJobGrade().getName() 
                    + " (hệ số " + oldCoefficient + ") lên cấp " 
                    + promotion.getRequestJobGrade().getName() 
                    + " (hệ số " + newCoefficient + "). "
                    + "Lương thay đổi từ " + currentBasicSalary + " lên " + newBasicSalary;

            Decision decision = new Decision();
            decision.setId("SP" + System.currentTimeMillis()); // ID dựa trên thời gian
            decision.setType(DecisionEnum.INCREASE_SALARY);
            decision.setDate(new java.util.Date()); // Ngày tạo quyết định
            decision.setEffectiveDate(effectiveDate); // Ngày hiệu lực (đầu tháng sau)
            decision.setContent(decisionContent);
            decision.setValue(((newCoefficient / oldCoefficient) - 1) * 100); // % tăng
            decision.setProcessed(false);
            decision.setUser(promotion.getUser());
            decision.setSigner(promotion.getSigner());
            decision.setSalaryPromotion(promotion);

            decisionRepository.save(decision);

            workLogRepository.save(WorkingHistory.builder()
                    .type(WorkLogTypeEnum.INCREASE_SALARY)
                    .user(decision.getUser())
                    .decision(decision)
                    .build());

        } catch (Exception e) {
            throw new AppException(ErrorCode.DECISION_CREATION_FAILED);
        }
    }
}
