package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.hr_management.dto.request.PositionRequest;
import org.ptithcm2021.hr_management.dto.response.PositionResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.PositionMapper;
import org.ptithcm2021.hr_management.model.Department;
import org.ptithcm2021.hr_management.model.Position;
import org.ptithcm2021.hr_management.repository.DepartmentRepository;
import org.ptithcm2021.hr_management.repository.PositionRepository;
import org.ptithcm2021.hr_management.service.PositionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;
    private final DepartmentRepository departmentRepository;

    @Override
    public PositionResponse createPosition(PositionRequest positionRequest) {
        if (positionRepository.existsByName(positionRequest.getName()))
            throw new AppException(ErrorCode.POSITION_NAME_EXISTS);

        Position position = positionMapper.toPosition(positionRequest);

        Department department = departmentRepository.findById(positionRequest.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        position.setDepartment(department);

        return positionMapper.toPositionResponse(positionRepository.save(position));
    }

    @Override
    public PositionResponse updatePosition(String positionId, PositionRequest positionRequest) {
        if (positionRepository.existsByName(positionRequest.getName()))
            throw new AppException(ErrorCode.POSITION_NAME_EXISTS);

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

        if (position.getDepartment().getId() != positionRequest.getDepartmentId()) {
            Department department = departmentRepository.findById(positionRequest.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

            position.setDepartment(department);
        }

        positionMapper.updatePosition(position, positionRequest);

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
