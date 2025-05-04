package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ContractService {
    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse createDraftContract(ContractRequest contractRequest) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse signContract(int contractId, String clause) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    void deleteContract(int contractId) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse extendContract(int contractId, ContractRequest contractRequest) throws Exception;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse getContract(int contractId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN') or T(String).valueOf(#userId) == authentication.name")
    PagedModel<ContractResponse> getAllContractByUser(long userId, ContractStatusEnum contractStatusEnum, Pageable pageable);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    PagedModel<ContractResponse> getAllContract(ContractStatusEnum contractStatusEnum, Pageable pageable);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    ContractResponse getContractIsActiveByUser(long userId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse updateContractWithPromotion(int contractId, String newJobGradeId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    ContractResponse updateContract(int contractId, ContractRequest contractRequest) throws Exception;

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    List<ContractResponse> getContractsByUserIdAndStatusNotActive(long userId);

    Contract getContractCurrentOfUser(long userId);

}
