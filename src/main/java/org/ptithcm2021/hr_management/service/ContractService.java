package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ContractService {
    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse createDraftContract(ContractRequest contractRequest) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse signContract(int contractId, String clause, boolean isExtend);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    void deleteContract(int contractId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse extendContract(int contractId, ContractRequest contractRequest) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse getContract(int contractId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN') or T(String).valueOf(#userId) == authentication.name")
    List<ContractResponse> getAllContractByUser(long userId, ContractStatusEnum contractStatusEnum);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    List<ContractResponse> getAllContract(ContractStatusEnum contractStatusEnum);

    Contract getContractCurrentOfUser(long userId);

    List<Contract> getAllContractIsActive();

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse updateContractWithPromotion(int contractId, String newJobGradeId);
}
