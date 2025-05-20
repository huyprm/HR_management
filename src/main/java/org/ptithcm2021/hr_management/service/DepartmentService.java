package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.DepartmentRequest;
import org.ptithcm2021.hr_management.dto.request.UpdateNameAndDescriptionRequest;
import org.ptithcm2021.hr_management.dto.response.DepartmentResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface DepartmentService {

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    DepartmentResponse createDepartment (DepartmentRequest departmentRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    DepartmentResponse updateDepartment (String id, UpdateNameAndDescriptionRequest request);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    void deleteDepartment(String departmentId);

    DepartmentResponse getDepartment (String departmentId);

    List<DepartmentResponse> getDepartments ();

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    DepartmentResponse getDepartmentByUserId (long userId);
}
