package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
import org.ptithcm2021.hr_management.dto.request.DecisionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
public interface DecisionService {
    DecisionResponse createDecision(DecisionRequest decisionRequest);
    DecisionResponse updateDecision(String id, DecisionUpdateRequest updateRequest);
    DecisionResponse getDecision(String id);
    void deleteDecision(String id);
    List<DecisionResponse> getAllDecisionByType(DecisionEnum decisionType);
}
