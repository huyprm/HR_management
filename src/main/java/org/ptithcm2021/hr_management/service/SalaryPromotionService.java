package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.SalaryPromotionRequest;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.SalaryPromotionResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.model.SalaryPromotion;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface SalaryPromotionService {
    @PreAuthorize("T(String).valueOf(#salaryPromotionRequest.getUserId()) == authentication.principal.username")
    SalaryPromotionResponse createSalaryPromotion(SalaryPromotionRequest salaryPromotionRequest);

    SalaryPromotionResponse getSalaryPromotionById(int id);

    @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN')")
    SalaryPromotionResponse updateSalaryPromotion(int id, SalaryPromotionUpdateRequest updateRequest);

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    void deleteSalaryPromotion(int id);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.principal.username")
    List<SalaryPromotionResponse> getSalaryPromotionByStatusAndUserId(long userId, FormStatusEnum formStatus);

    @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN')")
    List<SalaryPromotionResponse> getSalaryPromotionByStatusAndSignId(long signer, FormStatusEnum formStatus);

}
