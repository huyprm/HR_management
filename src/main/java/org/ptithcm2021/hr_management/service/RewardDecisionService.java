package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.RewardDecisionRequest;
import org.ptithcm2021.hr_management.dto.response.RewardDecisionResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
public interface RewardDecisionService {
    RewardDecisionResponse createRewardDecision(RewardDecisionRequest rewardDecisionRequest);
    RewardDecisionResponse updateRewardDecision(String id, RewardDecisionRequest rewardDecisionRequest);
    RewardDecisionResponse getRewardDecision(String id);
    List<RewardDecisionResponse> getAllRewardDecision();
    void deleteRewardDecision(String id);
}
