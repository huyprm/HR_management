package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.dto.response.SalaryPromotionResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.model.SalaryPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryPromotionRepository extends JpaRepository<SalaryPromotion, Integer> {
    List<SalaryPromotion> findAllByStatusAndUserId(FormStatusEnum status, long userId);
    List<SalaryPromotion> findAllByStatusAndSignerId(FormStatusEnum status, long signerId);

    List<SalaryPromotion> findAllByStatus(FormStatusEnum status);
}
