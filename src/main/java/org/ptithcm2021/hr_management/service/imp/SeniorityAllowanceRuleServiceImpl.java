package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.SeniorityAllowanceRuleRequest;
import org.ptithcm2021.hr_management.dto.response.SeniorityAllowanceRuleResponse;
import org.ptithcm2021.hr_management.enums.FetchStatus;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.SeniorityAllowanceRuleMapper;
import org.ptithcm2021.hr_management.model.SeniorityAllowanceRule;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.SeniorityAllowanceRuleRepository;
import org.ptithcm2021.hr_management.service.SeniorityAllowanceRuleService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeniorityAllowanceRuleServiceImpl implements SeniorityAllowanceRuleService {
    private final SeniorityAllowanceRuleRepository allowanceRuleRepository;
    private final SeniorityAllowanceRuleMapper allowanceRuleMapper;
    private final UserService userService;

    @Override
    public SeniorityAllowanceRuleResponse createAllowanceRule(SeniorityAllowanceRuleRequest seniorityAllowanceRuleRequest) {
        SeniorityAllowanceRule allowanceRule = allowanceRuleMapper.toSeniorityAllowanceRule(seniorityAllowanceRuleRequest);
        User user = userService.getUserToUser(seniorityAllowanceRuleRequest.getSignerId());

        allowanceRule.setSigner(user);

        return allowanceRuleMapper.toSeniorityAllowanceRuleResponse((allowanceRuleRepository.save(allowanceRule)));
    }

    @Override
    public SeniorityAllowanceRuleResponse updateAllowanceRule(SeniorityAllowanceRuleRequest seniorityAllowanceRuleRequest, int ruleId) {
        SeniorityAllowanceRule allowanceRule = allowanceRuleRepository.findById(ruleId)
                .orElseThrow(() -> new AppException(ErrorCode.SENIORITY_ALLOWANCE_RULE_NOT_FOUND));

        allowanceRuleMapper.updateAllowanceRule(allowanceRule, seniorityAllowanceRuleRequest);

        if (seniorityAllowanceRuleRequest.getSignerId() != allowanceRule.getSigner().getId()){
            User user = userService.getUserToUser(seniorityAllowanceRuleRequest.getSignerId());

            allowanceRule.setSigner(user);
        }

        return allowanceRuleMapper.toSeniorityAllowanceRuleResponse((allowanceRuleRepository.save(allowanceRule)));
    }

    @Override
    public void deleteAllowanceRule(int ruleId) {
        if (!allowanceRuleRepository.existsById(ruleId)) {
            throw new AppException(ErrorCode.SENIORITY_ALLOWANCE_RULE_NOT_FOUND);
        }

        try {
            allowanceRuleRepository.deleteById(ruleId);
        }catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }

    }

    @Override
    public SeniorityAllowanceRuleResponse getAllowanceRule(int ruleId) {
        SeniorityAllowanceRule allowanceRule = allowanceRuleRepository.findById(ruleId)
                .orElseThrow(() -> new AppException(ErrorCode.SENIORITY_ALLOWANCE_RULE_NOT_FOUND));

        return allowanceRuleMapper.toSeniorityAllowanceRuleResponse(allowanceRule);
    }

    @Override
    public List<SeniorityAllowanceRuleResponse> getAllAllowanceRule(FetchStatus fetchStatus) {
        if (FetchStatus.ALL.equals(fetchStatus)){
            return allowanceRuleRepository.findAll().stream()
                    .map(allowanceRuleMapper::toSeniorityAllowanceRuleResponse).toList();
        }

        if (FetchStatus.PENDING.equals(fetchStatus)){
            return allowanceRuleRepository.findAll().stream()
                    .filter(rule -> rule.getExpiryDate().after(new Date()))
                    .map(allowanceRuleMapper::toSeniorityAllowanceRuleResponse).toList();
        }

        if (FetchStatus.EXPIRED.equals(fetchStatus)){
            return allowanceRuleRepository.findAll().stream()
                    .filter(rule -> rule.getExpiryDate().before(new Date()))
                    .map(allowanceRuleMapper::toSeniorityAllowanceRuleResponse).toList();
        }

        return null;
    }
}
