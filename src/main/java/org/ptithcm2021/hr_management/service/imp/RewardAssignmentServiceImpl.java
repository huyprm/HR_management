package org.ptithcm2021.hr_management.service.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.AssignmentRequest;
import org.ptithcm2021.hr_management.dto.response.AssignmentResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.AssignmentMapper;
import org.ptithcm2021.hr_management.model.RewardAssignment;
import org.ptithcm2021.hr_management.model.AssignmentId;
import org.ptithcm2021.hr_management.model.RewardDecision;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.RewardAssignmentRepository;
import org.ptithcm2021.hr_management.repository.RewardDecisionRepository;
import org.ptithcm2021.hr_management.service.RewardAssignmentService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardAssignmentServiceImpl implements RewardAssignmentService {
    private final RewardDecisionRepository rewardDecisionRepository;
    private final UserService userService;
    private final RewardAssignmentRepository rewardAssignmentRepository;
    private final AssignmentMapper assignmentMapper;

    @Override
    public AssignmentResponse createRewardAssignment(AssignmentRequest request) {
        RewardDecision rewardDecision = rewardDecisionRepository.findById(request.getDecisionId())
                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));

        User user = userService.getUserToUser(request.getUserId());

        AssignmentId assignmentId = new AssignmentId(request.getUserId(), request.getDecisionId());
        RewardAssignment rewardAssignment = new RewardAssignment(assignmentId, rewardDecision, user);

        return assignmentMapper.toRewardAssignmentResponse(rewardAssignmentRepository.save(rewardAssignment));
    }

    @Transactional
    public AssignmentResponse updateRewardAssignment(AssignmentRequest request, String rewardDecisionId, long userId) {
        AssignmentId oldId = new AssignmentId(userId, rewardDecisionId);

        // Tìm kiếm bản ghi cũ
        RewardAssignment existingAssignment = rewardAssignmentRepository.findById(oldId)
                .orElseThrow(() -> new RuntimeException("Lỗi 1000"));

        // Xóa bản ghi cũ
        rewardAssignmentRepository.delete(existingAssignment);

        // Tạo bản ghi mới với userId mới
        long newUserId = request.getUserId() != null? request.getUserId(): existingAssignment.getUser().getId();
        String newRewardDecision = request.getDecisionId() != null? request.getDecisionId(): existingAssignment.getRewardDecision().getId();

        AssignmentId newId = new AssignmentId( newUserId, newRewardDecision);

        RewardAssignment newAssignment = new RewardAssignment();
        newAssignment.setId(newId);

        RewardDecision rewardDecision = rewardDecisionRepository.findById(newRewardDecision)
                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));
        newAssignment.setRewardDecision(rewardDecision);

        User newUser = userService.getUserToUser(newUserId);
        newAssignment.setUser(newUser);

        return assignmentMapper.toRewardAssignmentResponse(rewardAssignmentRepository.save(newAssignment));
    }


    @Override
    public AssignmentResponse getRewardAssignment(String rewardId, long userId) {
        AssignmentId temp = new AssignmentId(userId, rewardId);

        RewardAssignment rewardAssignment = rewardAssignmentRepository.findById(temp)
                .orElseThrow(() -> new AppException(ErrorCode.REWARD_ASSIGNMENT_NOT_FOUND));

        return assignmentMapper.toRewardAssignmentResponse(rewardAssignment);
    }

    @Override
    public List<AssignmentResponse> getAllRewardAssignmentByUser(long id) {
        List<RewardAssignment> rewardAssignments = rewardAssignmentRepository.findAllByUserId(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return rewardAssignments.stream().map(assignmentMapper::toRewardAssignmentResponse).toList();
    }

    @Override
    public void deleteRewardAssignment(String rewardId, long userId) {
        AssignmentId temp = new AssignmentId(userId, rewardId);
        if(rewardAssignmentRepository.existsById(temp)){
            rewardAssignmentRepository.deleteById(temp);
        } else{
            throw new AppException(ErrorCode.REWARD_ASSIGNMENT_NOT_FOUND);
        }
    }


}
