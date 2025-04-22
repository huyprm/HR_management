package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ContractService {
    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse createDraftContract(ContractRequest contractRequest) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse signContract(int contractId, String clause, boolean isExtend) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    void deleteContract(int contractId) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse extendContract(int contractId, ContractRequest contractRequest) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse getContract(int contractId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN') or T(String).valueOf(#userId) == authentication.name")
    Page<ContractResponse> getAllContractByUser(long userId, ContractStatusEnum contractStatusEnum, Pageable pageable);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    Page<ContractResponse> getAllContract(ContractStatusEnum contractStatusEnum, Pageable pageable);

    Contract getContractCurrentOfUser(long userId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse updateContractWithPromotion(int contractId, String newJobGradeId);

}
