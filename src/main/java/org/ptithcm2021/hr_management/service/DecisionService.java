package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
public interface DecisionService {
    DecisionResponse createDecision(DecisionRequest decisionRequest);
    DecisionResponse updateDecision(String id, DecisionRequest decisionRequest);
    DecisionResponse getDecision(String id);
    List<DecisionResponse> getAllDecision();
    void deleteDecision(String id);
}
