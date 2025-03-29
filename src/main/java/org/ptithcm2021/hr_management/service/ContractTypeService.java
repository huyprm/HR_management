package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.ContractTypeRequest;
import org.ptithcm2021.hr_management.dto.response.ContractTypeResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
public interface ContractTypeService {
    ContractTypeResponse createContractType(ContractTypeRequest contractTypeRequest);

    ContractTypeResponse updateContractType(int id, ContractTypeRequest contractTypeRequest);

    void deleteContractType(int id);

    ContractTypeResponse getContractType(int id);

    List<ContractTypeResponse> getAllContractType();
}
