package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.DepartmentRequest;
import org.ptithcm2021.hr_management.dto.request.UpdateNameAndDescriptionRequest;
import org.ptithcm2021.hr_management.dto.response.DepartmentResponse;
import org.ptithcm2021.hr_management.model.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(DepartmentRequest departmentRequest);
    DepartmentResponse toDepartmentResponse(Department department);

    void updateDepartment(@MappingTarget Department department, UpdateNameAndDescriptionRequest request);
}
