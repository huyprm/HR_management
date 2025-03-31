package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.RewardAssignment;
import org.ptithcm2021.hr_management.model.RewardDecision;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.RewardAssignmentRepository;
import org.ptithcm2021.hr_management.repository.RewardDecisionRepository;
import org.ptithcm2021.hr_management.service.RewardAssignmentService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardAssignmentServiceImpl implements RewardAssignmentService {
    private final RewardDecisionRepository rewardDecisionRepository;
    private final UserService userService;
    private final RewardAssignmentRepository rewardAssignmentRepository;
    @Override
    public RewardAssignment createRewardAssignment(String rewardId, long id) {
        RewardDecision rewardDecision = rewardDecisionRepository.findById(rewardId)
                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));

        User user = userService.getUserToUser(id);

        RewardAssignment rewardAssignment = RewardAssignment.builder()
                .rewardDecision(rewardDecision)
                .user(user).build();

        return rewardAssignmentRepository.save(rewardAssignment);
    }
}
