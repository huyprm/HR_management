package org.ptithcm2021.hr_management.service;


import org.ptithcm2021.hr_management.dto.request.RewardAssignmentRequest;
import org.ptithcm2021.hr_management.model.RewardAssignment;

public interface RewardAssignmentService {
    RewardAssignment createRewardAssignment(String rewardId, long id);
    RewardAssignment updateRewardAssignment(RewardAssignmentRequest rewardAssignmentRequest,
                                            String rewardId, long id);
    RewardAssignment getRewardAssignment(String rewardId, long id);
    RewardAssignment getAllRewardAssignmentByUser (long id);

}
