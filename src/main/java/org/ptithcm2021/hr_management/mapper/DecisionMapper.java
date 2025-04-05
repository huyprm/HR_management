package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
import org.ptithcm2021.hr_management.model.DisciplineDecision;
import org.ptithcm2021.hr_management.model.RewardDecision;

@Mapper(componentModel = "spring")
public interface DecisionMapper {
    RewardDecision toRewardDecision(DecisionRequest decisionRequest);
    DisciplineDecision toDisciplineDecision(DecisionRequest decisionRequest);

    @Mapping(target = "signer.id", source = "signer.id")
    @Mapping(target = "signer.fullName", source = "signer.fullName")
    DecisionResponse toRewardDecisionResponse(RewardDecision rewardDecision);

    @Mapping(target = "signer.id", source = "signer.id")
    @Mapping(target = "signer.fullName", source = "signer.fullName")
    DecisionResponse toDisciplineDecisionResponse(DisciplineDecision disciplineDecision);

    void updateRewardDecision(@MappingTarget RewardDecision rewardDecision, DecisionRequest decisionRequest);
    void updateDisciplineDecision(@MappingTarget DisciplineDecision disciplineDecision, DecisionRequest decisionRequest);
}
