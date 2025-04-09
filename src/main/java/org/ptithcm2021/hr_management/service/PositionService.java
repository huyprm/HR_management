package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.PositionRequest;
import org.ptithcm2021.hr_management.dto.request.UpdateNameAndDescriptionRequest;
import org.ptithcm2021.hr_management.dto.response.PositionResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
public interface PositionService {
    PositionResponse createPosition(PositionRequest positionRequest);

    PositionResponse updatePosition(String positionId, UpdateNameAndDescriptionRequest request);

    void deletePosition(String positionId);

    PositionResponse getPosition(String positionId);

    List<PositionResponse> getPositions();
}
