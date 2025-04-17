package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionRequest;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.SalaryPromotionResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.SalaryPromotionMapper;
import org.ptithcm2021.hr_management.model.SalaryPromotion;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.model.Decision;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.ptithcm2021.hr_management.repository.SalaryPromotionRepository;
import org.ptithcm2021.hr_management.repository.JobGradeRepository;
import org.ptithcm2021.hr_management.repository.DecisionRepository;
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
    private final JobGradeRepository jobGradeRepository;
    private final DecisionRepository decisionRepository;

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
        SalaryPromotion salaryPromotion = salaryPromotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND));

        User signer = userService.getUserToUser(updateRequest.getSignerId());

        salaryPromotion.setSigner(signer);
        salaryPromotion.setStatus(salaryPromotion.getStatus());

        return salaryPromotionMapper.toSalaryPromotionResponse(salaryPromotionRepository.save(salaryPromotion));
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
    
    @Override
    public SalaryPromotionResponse approveAndApplySalaryPromotion(int id, SalaryPromotionUpdateRequest updateRequest) {
        // 1. Lấy thông tin đề xuất tăng lương
        SalaryPromotion promotion = salaryPromotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND));
        
        // 2. Kiểm tra nếu đề xuất đã được xử lý trước đó
        if (promotion.getStatus() != FormStatusEnum.PENDING) {
            throw new AppException(ErrorCode.PROMOTION_ALREADY_PROCESSED);
        }
        
        // 3. Cập nhật người duyệt và ghi chú
        User signer = userService.getUserToUser(updateRequest.getSignerId());
        promotion.setSigner(signer);
        promotion.setNote(updateRequest.getReason());
        
        // 4. Cập nhật trạng thái đề xuất
        promotion.setStatus(updateRequest.getFormStatus());
        
        // 5. Nếu đề xuất được phê duyệt, thực hiện tăng lương và thay đổi JobGrade
        if (updateRequest.getFormStatus() == FormStatusEnum.APPROVED) {
            applyApprovedSalaryPromotion(promotion);
        }
        
        // 6. Lưu đề xuất đã cập nhật
        promotion = salaryPromotionRepository.save(promotion);
        
        return salaryPromotionMapper.toSalaryPromotionResponse(promotion);
    }

    /**
     * Áp dụng thay đổi khi đề xuất tăng lương được phê duyệt
     * Thay vì áp dụng ngay, tạo một Decision với ngày hiệu lực vào đầu tháng sau
     */
    private void applyApprovedSalaryPromotion(SalaryPromotion promotion) {
        try {
            // 1. Lấy hợp đồng hiện tại
            var contract = contractService.getContractCurrentOfUser(promotion.getUser().getId());
            
            // 2. Tính toán mức lương mới dựa trên hệ số
            double oldCoefficient = promotion.getCurrentJobGrade().getCoefficient();
            double newCoefficient = promotion.getRequestJobGrade().getCoefficient();
            double currentBasicSalary = contract.getBasicSalary();
            double newBasicSalary = (currentBasicSalary / oldCoefficient) * newCoefficient;
            
            // 3. Tính ngày đầu tiên của tháng sau để đặt làm ngày hiệu lực
            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate firstDayOfNextMonth = today.withDayOfMonth(1).plusMonths(1);
            java.util.Date effectiveDate = java.sql.Date.valueOf(firstDayOfNextMonth);
            
            // 4. Chuẩn bị nội dung quyết định
            String decisionContent = "Quyết định tăng lương cho " + promotion.getUser().getFullName() 
                    + " từ cấp " + promotion.getCurrentJobGrade().getName() 
                    + " (hệ số " + oldCoefficient + ") lên cấp " 
                    + promotion.getRequestJobGrade().getName() 
                    + " (hệ số " + newCoefficient + "). "
                    + "Lương cơ bản thay đổi từ " + currentBasicSalary + " lên " + newBasicSalary;
                    
            // 5. Tạo mới quyết định
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
            
            // 6. Lưu quyết định
            decisionRepository.save(decision);
        } catch (Exception e) {
            throw new AppException(ErrorCode.DECISION_CREATION_FAILED);
        }
    }
}
