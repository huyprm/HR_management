package org.ptithcm2021.hr_management.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
import org.ptithcm2021.hr_management.dto.request.DecisionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.ptithcm2021.hr_management.enums.WorkLogTypeEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.DecisionMapper;
import org.ptithcm2021.hr_management.model.*;
import org.ptithcm2021.hr_management.repository.*;
import org.ptithcm2021.hr_management.service.DecisionService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DecisionServiceImpl implements DecisionService {
    private final DecisionRepository decisionRepository;
    private final DecisionMapper decisionMapper;
    private final UserService userService;
    private final PositionRepository positionRepository;
    private final SeniorityAllowanceRuleRepository seniorityAllowanceRuleRepository;
    private final WorkLogRepository workLogRepository;
    private final SalaryPromotionRepository salaryPromotionRepository;

    @Override
    public DecisionResponse createDecision(DecisionRequest decisionRequest) {
        if (decisionRepository.existsById(decisionRequest.getId())) {
            throw new AppException(ErrorCode.DECISION_ALREADY_EXISTS);
        }

        User user = userService.getUserToUser(decisionRequest.getUserId());

        Decision decision = decisionMapper.toDecision(decisionRequest);

        decision.setUser(user);

        if (decisionRequest.getPositionId() != null) {
            Position position = positionRepository.findById(decisionRequest.getPositionId())
                    .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

            decision.setPosition(position);
            decision.setProcessed(false);

        } else if (decisionRequest.getSeniorityAllowanceRuleId() != null) {
            SeniorityAllowanceRule seniorityAllowanceRule = seniorityAllowanceRuleRepository.findById(decisionRequest.getSeniorityAllowanceRuleId())
                    .orElseThrow(() -> new AppException(ErrorCode.SENIORITY_ALLOWANCE_RULE_NOT_FOUND));

            decision.setSeniorityAllowanceRule(seniorityAllowanceRule);
            decision.setProcessed(false);
        } else if (decisionRequest.getSalaryPromotionId() != null) {
            SalaryPromotion salaryPromotion = salaryPromotionRepository.findById(decisionRequest.getSalaryPromotionId())
                    .orElseThrow(() -> new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND));

            decision.setSalaryPromotion(salaryPromotion);
            decision.setProcessed(false);
        }

        return decisionMapper.toDecisionResponse(decisionRepository.save(decision));
    }

    @Override
    @Transactional
    public DecisionResponse updateDecision(String id, DecisionUpdateRequest updateRequest) {
        Decision decision = decisionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DECISION_NOT_FOUND));

        decisionMapper.updateDecision(decision, updateRequest);

        User signer = userService.getUserToUser(updateRequest.getSignerId());
        decision.setSigner(signer);

        WorkLogTypeEnum workLogType = switch (decision.getType()) {
            case INCREASE_SALARY -> WorkLogTypeEnum.INCREASE_SALARY;
            case PROMOTION -> WorkLogTypeEnum.PROMOTION;
            case SENIORITY_ALLOWANCE -> WorkLogTypeEnum.SENIORITY_ALLOWANCE;
            case TERMINATION -> WorkLogTypeEnum.TERMINATION;
            case DISCIPLINE -> WorkLogTypeEnum.DISCIPLINE;
            case AWARD -> WorkLogTypeEnum.AWARD;
        };

        workLogRepository.save(
                WorkingHistory.builder()
                        .type(workLogType)
                        .user(decision.getUser())
                        .decision(decision).build()
        );
        return decisionMapper.toDecisionResponse(decisionRepository.save(decision));
    }

    @Override
    public DecisionResponse getDecision(String id) {
        Decision decision = decisionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DECISION_NOT_FOUND));

        return decisionMapper.toDecisionResponse(decision);
    }

    @Override
    public void deleteDecision(String id) {
        try {
            Decision decision = decisionRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.DECISION_NOT_FOUND));

            if (decision.getSigner() != null) throw new AppException(ErrorCode.CANNOT_BE_DELETED);

            decisionRepository.delete(decision);
        }catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }

    @Override
    public List<DecisionResponse> getAllDecisionByType(DecisionEnum decisionType) {
        if(decisionType == null) {
            return decisionRepository.findAll()
                    .stream().map(decisionMapper::toDecisionResponse).collect(Collectors.toList());
        }
        return decisionRepository.findAllByType(decisionType)
                .stream().map(decisionMapper::toDecisionResponse).collect(Collectors.toList());
    }

    @Override
    public List<DecisionResponse> getAllDecisionByUser(long userId, DecisionEnum decisionType) {
        if(decisionType == null) {
            return decisionRepository.findAllByUserId(userId)
                    .stream().map(decisionMapper::toDecisionResponse).collect(Collectors.toList());
        }
        return decisionRepository.findAllByUserIdAndType(userId, decisionType)
                .stream().map(decisionMapper::toDecisionResponse).collect(Collectors.toList());
    }
}
