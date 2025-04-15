package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.SeniorityAllowanceRuleRequest;
import org.ptithcm2021.hr_management.dto.response.SeniorityAllowanceRuleResponse;
import org.ptithcm2021.hr_management.enums.FetchStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface SeniorityAllowanceRuleService {
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    SeniorityAllowanceRuleResponse createAllowanceRule(SeniorityAllowanceRuleRequest seniorityAllowanceRuleRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    SeniorityAllowanceRuleResponse updateAllowanceRule(SeniorityAllowanceRuleRequest seniorityAllowanceRuleRequest, int ruleId);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    void deleteAllowanceRule(int ruleId);

    SeniorityAllowanceRuleResponse getAllowanceRule(int ruleId);

    List<SeniorityAllowanceRuleResponse> getAllAllowanceRule(FetchStatus fetchStatus);

}
