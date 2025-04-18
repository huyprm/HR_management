package org.ptithcm2021.hr_management.service.impl;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.DepartmentRequest;
import org.ptithcm2021.hr_management.dto.request.UpdateNameAndDescriptionRequest;
import org.ptithcm2021.hr_management.dto.response.DepartmentResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.DepartmentMapper;
import org.ptithcm2021.hr_management.model.Department;
import org.ptithcm2021.hr_management.repository.DepartmentRepository;
import org.ptithcm2021.hr_management.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        if(departmentRepository.existsById(departmentRequest.getId()))
            throw new AppException(ErrorCode.DEPARTMENT_ID_EXIST);

        if (departmentRepository.existsByName(departmentRequest.getName())){
            throw new AppException(ErrorCode.DEPARTMENT_NAME_EXIST);
        }
        Department department = departmentMapper.toDepartment(departmentRequest);

        departmentRepository.save(department);
        return departmentMapper.toDepartmentResponse(department);
    }

    @Override
    public DepartmentResponse updateDepartment(String id, UpdateNameAndDescriptionRequest request) {
//        if (departmentRepository.existsByName(request.getName())){
//            throw new AppException(ErrorCode.DEPARTMENT_NAME_EXIST);
//        }

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        departmentMapper.updateDepartment(department, request);

        departmentRepository.save(department);

        return departmentMapper.toDepartmentResponse(department);
    }

    @Override
    public void deleteDepartment(String departmentId) {
        try {
            departmentRepository.deleteById(departmentId);
        }catch (Exception e){
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }

    @Override
    public DepartmentResponse getDepartment(String departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        return departmentMapper.toDepartmentResponse(department);
    }

    @Override
    public List<DepartmentResponse> getDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream().map(departmentMapper::toDepartmentResponse).toList();
    }
}
