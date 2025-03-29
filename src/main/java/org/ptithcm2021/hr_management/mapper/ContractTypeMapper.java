package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.ContractTypeRequest;
import org.ptithcm2021.hr_management.dto.response.ContractTypeResponse;
import org.ptithcm2021.hr_management.model.ContractType;

@Mapper(componentModel = "spring")
public interface ContractTypeMapper {
    ContractTypeResponse toContractTypeResponse(ContractType contractType);

    ContractType toContractType(ContractTypeRequest contractTypeRequest);

    void updateContractType (@MappingTarget ContractType contractType, ContractTypeRequest contractTypeRequest);
}
