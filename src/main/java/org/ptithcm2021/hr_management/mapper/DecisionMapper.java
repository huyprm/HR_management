package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
import org.ptithcm2021.hr_management.dto.request.DecisionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
import org.ptithcm2021.hr_management.model.Decision;
//import org.ptithcm2021.hr_management.model.DisciplineDecision;
//import org.ptithcm2021.hr_management.model.RewardDecision;

@Mapper(componentModel = "spring", uses = {
        SeniorityAllowanceRuleMapper.class,
        PositionMapper.class,
})
public interface DecisionMapper {
//    RewardDecision toRewardDecision(DecisionRequest decisionRequest);
//    DisciplineDecision toDisciplineDecision(DecisionRequest decisionRequest);

    Decision toDecision(DecisionRequest decisionRequest);

    @Mapping(target = "signer.id", source = "signer.id")
    @Mapping(target = "signer.fullName", source = "signer.fullName")
    @Mapping(target = "user.id", source = "user.id")
    @Mapping(target = "user.fullName", source = "user.fullName")
    DecisionResponse toDecisionResponse(Decision decision);

//    @Mapping(target = "signer.id", source = "signer.id")
//    @Mapping(target = "signer.fullName", source = "signer.fullName")
//    DecisionResponse toRewardDecisionResponse(RewardDecision rewardDecision);
//
//    @Mapping(target = "signer.id", source = "signer.id")
//    @Mapping(target = "signer.fullName", source = "signer.fullName")
//    DecisionResponse toDisciplineDecisionResponse(DisciplineDecision disciplineDecision);

    void updateDecision(@MappingTarget Decision decision, DecisionUpdateRequest updateRequest);
//    void updateRewardDecision(@MappingTarget RewardDecision rewardDecision, DecisionRequest decisionRequest);
//    void updateDisciplineDecision(@MappingTarget DisciplineDecision disciplineDecision, DecisionRequest decisionRequest);
}
