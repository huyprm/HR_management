package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.PositionRequest;
import org.ptithcm2021.hr_management.dto.request.UpdateNameAndDescriptionRequest;
import org.ptithcm2021.hr_management.dto.response.PositionResponse;
import org.ptithcm2021.hr_management.model.Position;

@Mapper(componentModel = "spring", uses = DepartmentMapper.class)
public interface PositionMapper {
    Position toPosition(PositionRequest positionRequest);

    PositionResponse toPositionResponse(Position position);

    void updatePosition(@MappingTarget Position position, UpdateNameAndDescriptionRequest request);

}
