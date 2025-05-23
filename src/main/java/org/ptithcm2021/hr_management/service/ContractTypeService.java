package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.ContractTypeRequest;
import org.ptithcm2021.hr_management.dto.response.ContractTypeResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_STAFF')")
public interface ContractTypeService {
    ContractTypeResponse createContractType(ContractTypeRequest contractTypeRequest);

    void deleteContractType(String id);

    ContractTypeResponse getContractType(String id);

    List<ContractTypeResponse> getAllContractType();
}
