package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.PositionRequest;
import org.ptithcm2021.hr_management.dto.request.UpdateNameAndDescriptionRequest;
import org.ptithcm2021.hr_management.dto.response.PositionResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.PositionMapper;
import org.ptithcm2021.hr_management.model.Department;
import org.ptithcm2021.hr_management.model.Position;
import org.ptithcm2021.hr_management.model.Role;
import org.ptithcm2021.hr_management.repository.DepartmentRepository;
import org.ptithcm2021.hr_management.repository.PositionRepository;
import org.ptithcm2021.hr_management.repository.RoleRepository;
import org.ptithcm2021.hr_management.service.PositionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;

    @Override
    public PositionResponse createPosition(PositionRequest positionRequest) {
        if(positionRepository.existsById(positionRequest.getDepartmentId()))
            throw new AppException(ErrorCode.POSITION_ID_EXISTS);

        if (positionRepository.existsByName(positionRequest.getName()))
            throw new AppException(ErrorCode.POSITION_NAME_EXISTS);

        Position position = positionMapper.toPosition(positionRequest);

        Department department = departmentRepository.findById(positionRequest.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        Role role = roleRepository.findById(positionRequest.getRoleId()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        position.setDepartment(department);
        position.setRole(role);

        return positionMapper.toPositionResponse(positionRepository.save(position));
    }

    @Override
    public PositionResponse updatePosition(String positionId, UpdateNameAndDescriptionRequest request) {
//        if(positionRepository.existsById(request.getName()))
//            throw new AppException(ErrorCode.POSITION_NAME_EXISTS);

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

        positionMapper.updatePosition(position, request);

        return positionMapper.toPositionResponse(positionRepository.save(position));
    }

    @Override
    public void deletePosition(String positionId) {
        if (!positionRepository.existsById(positionId)) {
            throw new AppException(ErrorCode.POSITION_NOT_FOUND);
        }

        try {
            positionRepository.deleteById(positionId);
        }catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }

    @Override
    public PositionResponse getPosition(String positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

        return positionMapper.toPositionResponse(position);
    }

    @Override
    public List<PositionResponse> getPositions() {
        return positionRepository.findAll().stream().map(positionMapper::toPositionResponse).toList();
    }
}
