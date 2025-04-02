package org.ptithcm2021.hr_management.service;


import org.ptithcm2021.hr_management.dto.request.AssignmentRequest;
import org.ptithcm2021.hr_management.dto.response.AssignmentResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RewardAssignmentService {
    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    AssignmentResponse createRewardAssignment(AssignmentRequest assignmentRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    AssignmentResponse updateRewardAssignment(AssignmentRequest assignmentRequest, String rewardDecisionId, long userId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN') or #userId == authentication.name")
    AssignmentResponse getRewardAssignment(String rewardId, long userId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN') or #userId == authentication.name")
    List<AssignmentResponse> getAllRewardAssignmentByUser (long userId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    void deleteRewardAssignment(String rewardId, long userId);
}
