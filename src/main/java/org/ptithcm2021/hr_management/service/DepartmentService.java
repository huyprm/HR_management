package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.DepartmentRequest;
import org.ptithcm2021.hr_management.dto.response.DepartmentResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
@PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
public interface DepartmentService {

    DepartmentResponse createDepartment (DepartmentRequest departmentRequest);

    DepartmentResponse updateDepartment (int id, DepartmentRequest departmentRequest);

    void deleteDepartment(int departmentId);

    DepartmentResponse getDepartment (int departmentId);

    List<DepartmentResponse> getDepartments ();
}
