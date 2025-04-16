package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionRequest;
import org.ptithcm2021.hr_management.dto.response.SalaryPromotionResponse;
import org.ptithcm2021.hr_management.model.SalaryPromotion;

@Mapper(componentModel = "spring")
public interface SalaryPromotionMapper {
    SalaryPromotion toSalaryPromotion(SalaryPromotionRequest salaryPromotionRequest);

    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "signerName", source = "signer.fullName")
    @Mapping(target = "currentJobGradeName", source = "currentJobGrade.name")
    @Mapping(target = "requestJobGradeName", source = "requestJobGrade.name")
    @Mapping(target = "requestJobGradeValue", source = "requestJobGrade.coefficient")
    SalaryPromotionResponse toSalaryPromotionResponse(SalaryPromotion salaryPromotion);
}
