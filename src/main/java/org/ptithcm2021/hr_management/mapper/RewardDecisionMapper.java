package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.RewardDecisionRequest;
import org.ptithcm2021.hr_management.dto.response.RewardDecisionResponse;
import org.ptithcm2021.hr_management.model.RewardDecision;

@Mapper(componentModel = "spring")
public interface RewardDecisionMapper {
    RewardDecision toRewardDecision(RewardDecisionRequest rewardDecisionRequest);

    @Mapping(target = "signer.id", source = "sender.id")
    @Mapping(target = "signer.fullName", source = "sender.fullName")
    RewardDecisionResponse toRewardDecisionResponse(RewardDecision rewardDecision);

    void updateRewardDecision(@MappingTarget RewardDecision rewardDecision, RewardDecisionRequest rewardDecisionRequest);
}
