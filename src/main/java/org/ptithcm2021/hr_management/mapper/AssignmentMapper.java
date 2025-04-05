package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ptithcm2021.hr_management.dto.response.AssignmentResponse;
import org.ptithcm2021.hr_management.model.DisciplineAssignment;
import org.ptithcm2021.hr_management.model.RewardAssignment;

@Mapper(componentModel = "spring", uses = DecisionMapper.class)
public interface AssignmentMapper {
    @Mapping(target = "userSummaryResponse.id", source = "user.id")
    @Mapping(target = "userSummaryResponse.fullName", source = "user.fullName")
    @Mapping(target = "type", source = "rewardDecision.type")
    @Mapping(target = "content", source = "rewardDecision.content")
    @Mapping(target = "date", source = "rewardDecision.date")
    AssignmentResponse toRewardAssignmentResponse(RewardAssignment rewardAssignment);

    @Mapping(target = "userSummaryResponse.id", source = "user.id")
    @Mapping(target = "userSummaryResponse.fullName", source = "user.fullName")
    @Mapping(target = "type", source = "disciplineDecision.type")
    @Mapping(target = "content", source = "disciplineDecision.content")
    @Mapping(target = "date", source = "disciplineDecision.date")
    AssignmentResponse toDisciplinedAssignmentResponse(DisciplineAssignment disciplineAssignment);

    //void updateRewardAssignment(@MappingTarget RewardAssignment rewardAssignment, RewardAssignmentRequest rewardAssignmentRequest);
}