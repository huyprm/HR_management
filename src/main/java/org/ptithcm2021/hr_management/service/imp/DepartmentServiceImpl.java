package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.DepartmentRequest;
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
        Department department = departmentMapper.toDepartment(departmentRequest);

        departmentRepository.save(department);
        return departmentMapper.toDepartmentResponse(department);
    }

    @Override
    public DepartmentResponse updateDepartment(long id, DepartmentRequest departmentRequest) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        departmentMapper.updateDepartment(department, departmentRequest);

        departmentRepository.save(department);

        return departmentMapper.toDepartmentResponse(department);
    }

    @Override
    public void deleteDepartment(long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        department.setDeleted(true);

        departmentRepository.save(department);
    }

    @Override
    public DepartmentResponse getDepartment(long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        return departmentMapper.toDepartmentResponse(department);
    }

    @Override
    public List<DepartmentResponse> getDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream().filter(department -> !department.isDeleted()).map(departmentMapper::toDepartmentResponse).toList();
    }
}
