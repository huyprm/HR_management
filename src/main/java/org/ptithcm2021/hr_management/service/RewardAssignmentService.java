package org.ptithcm2021.hr_management.service;


import org.ptithcm2021.hr_management.model.RewardAssignment;

public interface RewardAssignmentService {
    RewardAssignment createRewardAssignment(String rewardId, long id);
}
