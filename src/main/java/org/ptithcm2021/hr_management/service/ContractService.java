package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ContractService {
    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse createDraftContract(ContractRequest contractRequest, boolean isExtend);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse signContract(int contractId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse createContract(ContractRequest contractRequest, boolean isExtend);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse getContract(int contractId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    List<ContractResponse> getAllContractByUser(long userId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    List<ContractResponse> getAllContract(ContractStatusEnum contractStatusEnum);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    void deleteContract(int contractId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse extendContract(int contractId, ContractRequest contractRequest);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    ContractResponse getContractIsPendingByUserId(long userId);

    Contract getContractCurrentOfUser(long userId);

    List<Contract> getAllContractIsActive();

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse updateContractWithPromotion(int contractId, String newJobGradeId);
}
