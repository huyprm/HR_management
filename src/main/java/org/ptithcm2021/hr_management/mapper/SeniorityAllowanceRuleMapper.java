package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.SeniorityAllowanceRuleRequest;
import org.ptithcm2021.hr_management.dto.response.SeniorityAllowanceRuleResponse;
import org.ptithcm2021.hr_management.model.SeniorityAllowanceRule;

@Mapper(componentModel = "spring")
public interface SeniorityAllowanceRuleMapper {
    SeniorityAllowanceRule toSeniorityAllowanceRule(SeniorityAllowanceRuleRequest seniorityAllowanceRuleRequest);

    @Mapping(source = "signer.id", target = "signer.id")
    @Mapping(source ="signer.fullName", target = "signer.fullName")
    SeniorityAllowanceRuleResponse toSeniorityAllowanceRuleResponse(SeniorityAllowanceRule seniorityAllowanceRule);

    void updateAllowanceRule (@MappingTarget SeniorityAllowanceRule seniorityAllowanceRule,
                              SeniorityAllowanceRuleRequest seniorityAllowanceRuleRequest);
}
