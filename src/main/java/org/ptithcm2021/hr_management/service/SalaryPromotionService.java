package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.SalaryPromotionRequest;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.SalaryPromotionResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.model.SalaryPromotion;

import java.util.List;

public interface SalaryPromotionService {
    SalaryPromotionResponse createSalaryPromotion(SalaryPromotionRequest salaryPromotionRequest);
    SalaryPromotionResponse getSalaryPromotionById(int id);
    SalaryPromotionResponse updateSalaryPromotion(int id, SalaryPromotionUpdateRequest updateRequest);
    void deleteSalaryPromotion(int id);
    List<SalaryPromotionResponse> getSalaryPromotionByStatusAndUserId(long userId, FormStatusEnum formStatus);
    List<SalaryPromotionResponse> getSalaryPromotionByStatusAndSignId(long signer, FormStatusEnum formStatus);
}
