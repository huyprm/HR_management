//package org.ptithcm2021.hr_management.service.imp;
//
//import lombok.RequiredArgsConstructor;
//import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
//import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
//import org.ptithcm2021.hr_management.exception.AppException;
//import org.ptithcm2021.hr_management.exception.ErrorCode;
//import org.ptithcm2021.hr_management.mapper.DecisionMapper;
//import org.ptithcm2021.hr_management.model.RewardDecision;
//import org.ptithcm2021.hr_management.model.User;
//import org.ptithcm2021.hr_management.repository.RewardDecisionRepository;
//import org.ptithcm2021.hr_management.service.DecisionService;
//import org.ptithcm2021.hr_management.service.UserService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service("rewardDecisionServiceImpl")
//@RequiredArgsConstructor
//public class RewardDecisionServiceImpl implements DecisionService {
//    private final RewardDecisionRepository rewardDecisionRepository;
//    private final DecisionMapper decisionMapper;
//    private final UserService userService;
//
//    @Override
//    public DecisionResponse createDecision(DecisionRequest decisionRequest) {
//        User user = userService.getUserToUser(decisionRequest.getSignerId());
//
//        RewardDecision rewardDecision = decisionMapper.toRewardDecision(decisionRequest);
//        rewardDecision.setSigner(user);
//
//        return decisionMapper.toRewardDecisionResponse(rewardDecisionRepository.save(rewardDecision));
//    }
//
//    @Override
//    public DecisionResponse updateDecision(String id, DecisionRequest decisionRequest) {
//        RewardDecision rewardDecision = rewardDecisionRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));
//
//        decisionMapper.updateRewardDecision(rewardDecision, decisionRequest);
//
//        if (rewardDecision.getSigner().getId()!= decisionRequest.getSignerId()){
//            User user = userService.getUserToUser(decisionRequest.getSignerId());
//            rewardDecision.setSigner(user);
//        }
//        return decisionMapper.toRewardDecisionResponse(rewardDecisionRepository.save(rewardDecision));
//    }
//
//    @Override
//    public DecisionResponse getDecision(String id) {
//        RewardDecision rewardDecision = rewardDecisionRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));
//
//        return decisionMapper.toRewardDecisionResponse(rewardDecision);
//    }
//
//    @Override
//    public List<DecisionResponse> getAllDecision() {
//        List<RewardDecision> rewardDecisions = rewardDecisionRepository.findAll();
//        return rewardDecisions.stream()
//                .map(decisionMapper::toRewardDecisionResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void deleteDecision(String id) {
//        if (!rewardDecisionRepository.existsById(id)) {
//            throw new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND);
//        }
//
//        try {
//            rewardDecisionRepository.deleteById(id);
//        }catch (Exception e) {
//            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
//        }
//    }
//}
