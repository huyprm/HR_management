package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.RewardDecisionRequest;
import org.ptithcm2021.hr_management.dto.response.RewardDecisionResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.RewardDecisionMapper;
import org.ptithcm2021.hr_management.model.RewardDecision;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.RewardDecisionRepository;
import org.ptithcm2021.hr_management.service.RewardDecisionService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardDecisionServiceImp implements RewardDecisionService {
    private final RewardDecisionRepository rewardDecisionRepository;
    private final RewardDecisionMapper rewardDecisionMapper;
    private final UserService userService;

    @Override
    public RewardDecisionResponse createRewardDecision(RewardDecisionRequest rewardDecisionRequest) {
        User user = userService.getUserToUser(rewardDecisionRequest.getSignerId());

        RewardDecision rewardDecision = rewardDecisionMapper.toRewardDecision(rewardDecisionRequest);
        rewardDecision.setSigner(user);

        return rewardDecisionMapper.toRewardDecisionResponse(rewardDecisionRepository.save(rewardDecision));
    }

    @Override
    public RewardDecisionResponse updateRewardDecision(String id, RewardDecisionRequest rewardDecisionRequest) {
        RewardDecision rewardDecision = rewardDecisionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));

        rewardDecisionMapper.updateRewardDecision(rewardDecision, rewardDecisionRequest);

        if (rewardDecision.getSigner().getId()!= rewardDecisionRequest.getSignerId()){
            User user = userService.getUserToUser(rewardDecisionRequest.getSignerId());
            rewardDecision.setSigner(user);
        }
        return rewardDecisionMapper.toRewardDecisionResponse(rewardDecisionRepository.save(rewardDecision));
    }

    @Override
    public RewardDecisionResponse getRewardDecision(String id) {
        RewardDecision rewardDecision = rewardDecisionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));

        return rewardDecisionMapper.toRewardDecisionResponse(rewardDecision);
    }

    @Override
    public List<RewardDecisionResponse> getAllRewardDecision() {
        List<RewardDecision> rewardDecisions = rewardDecisionRepository.findAll();
        return rewardDecisions.stream()
                .map(rewardDecisionMapper::toRewardDecisionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRewardDecision(String id) {
        if (!rewardDecisionRepository.existsById(id)) {
            throw new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND);
        }

        try {
            rewardDecisionRepository.deleteById(id);
        }catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }
}
