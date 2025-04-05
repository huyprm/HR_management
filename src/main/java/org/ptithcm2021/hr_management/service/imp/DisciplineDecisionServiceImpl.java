package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.DecisionMapper;
import org.ptithcm2021.hr_management.model.DisciplineDecision;
import org.ptithcm2021.hr_management.model.RewardDecision;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.DisciplineDecisionRepository;
import org.ptithcm2021.hr_management.repository.RewardDecisionRepository;
import org.ptithcm2021.hr_management.service.DecisionService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("disciplineDecisionServiceImpl")
@RequiredArgsConstructor
public class DisciplineDecisionServiceImpl implements DecisionService {
    private final DisciplineDecisionRepository disciplineDecisionRepository;
    private final DecisionMapper decisionMapper;
    private final UserService userService;

    @Override
    public DecisionResponse createDecision(DecisionRequest decisionRequest) {
        User user = userService.getUserToUser(decisionRequest.getSignerId());

        DisciplineDecision decision = decisionMapper.toDisciplineDecision(decisionRequest);
        decision.setSigner(user);

        return decisionMapper.toDisciplineDecisionResponse(disciplineDecisionRepository.save(decision));
    }

    @Override
    public DecisionResponse updateDecision(String id, DecisionRequest decisionRequest) {
        DisciplineDecision decision = disciplineDecisionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));

        decisionMapper.updateDisciplineDecision(decision, decisionRequest);

        if (decision.getSigner().getId()!= decisionRequest.getSignerId()){
            User user = userService.getUserToUser(decisionRequest.getSignerId());
            decision.setSigner(user);
        }
        return decisionMapper.toDisciplineDecisionResponse(disciplineDecisionRepository.save(decision));
    }

    @Override
    public DecisionResponse getDecision(String id) {
        DisciplineDecision decision = disciplineDecisionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND));

        return decisionMapper.toDisciplineDecisionResponse(decision);
    }

    @Override
    public List<DecisionResponse> getAllDecision() {
        List<DisciplineDecision> decisions = disciplineDecisionRepository.findAll();

        return decisions.stream()
                .map(decisionMapper::toDisciplineDecisionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDecision(String id) {
        if (!disciplineDecisionRepository.existsById(id)) {
            throw new AppException(ErrorCode.REWARD_DECISION_NOT_FOUND);
        }

        try {
            disciplineDecisionRepository.deleteById(id);
        }catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }
}
