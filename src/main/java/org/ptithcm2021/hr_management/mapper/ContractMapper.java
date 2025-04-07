package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.model.Contract;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    Contract toContract(ContractRequest contractRequest);

    ContractResponse toContractResponse(Contract contract);

}
