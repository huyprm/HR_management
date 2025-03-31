package org.ptithcm2021.hr_management.service.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.RewardAssignmentRequest;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.RewardAssignment;
import org.ptithcm2021.hr_management.model.RewardAssignmentId;
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

        RewardAssignmentId rewardAssignmentId = new RewardAssignmentId(id, rewardId);
        RewardAssignment rewardAssignment = new RewardAssignment(rewardAssignmentId, rewardDecision, user);



        return rewardAssignmentRepository.save(rewardAssignment);
    }

    @Transactional
    public RewardAssignment updateRewardAssignment(RewardAssignmentRequest request, String rewardDecisionId, long userId) {
        RewardAssignmentId oldId = new RewardAssignmentId(userId, rewardDecisionId);

        // Tìm kiếm bản ghi cũ
        RewardAssignment existingAssignment = rewardAssignmentRepository.findById(oldId)
                .orElseThrow(() -> new RuntimeException("Lỗi 1000"));

        // Xóa bản ghi cũ
        rewardAssignmentRepository.delete(existingAssignment);

        // Tạo bản ghi mới với userId mới
        long newUserId = request.getUserId() != null? request.getUserId(): existingAssignment.getUser().getId();
        String newRewardDecision = request.getRewardDecisionId() != null? request.getRewardDecisionId(): existingAssignment.getRewardDecision().getId();

        RewardAssignmentId newId = new RewardAssignmentId( newUserId, newRewardDecision);

        RewardAssignment newAssignment = new RewardAssignment();
        newAssignment.setId(newId);

        RewardDecision rewardDecision = rewardDecisionRepository.findById(newRewardDecision)
                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));
        newAssignment.setRewardDecision(rewardDecision);

        User newUser = userService.getUserToUser(newUserId);
        newAssignment.setUser(newUser);

        return rewardAssignmentRepository.save(newAssignment);
    }


    @Override
    public RewardAssignment getRewardAssignment(String rewardId, long id) {
        RewardAssignmentId temp = new RewardAssignmentId(id, rewardId);

        return rewardAssignmentRepository.findById(temp)
                .orElseThrow(() -> new RuntimeException("lỗi 1000"));
    }

    @Override
    public RewardAssignment getAllRewardAssignmentByUser(long id) {
        return null;
    }


}
