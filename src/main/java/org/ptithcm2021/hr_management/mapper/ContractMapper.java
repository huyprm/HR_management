package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.model.Contract;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    Contract toContract(ContractRequest contractRequest);

    @Mapping(target = "signer.id", source = "signer.id")
    @Mapping(target = "signer.fullName", source = "signer.fullName")
    @Mapping(target = "user.id", source = "user.id")
    @Mapping(target = "user.fullName", source = "user.fullName")
    @Mapping(target = "positionName", source = "position.name")
    @Mapping(target = "jobGradeName", source = "jobGrade.name")
    @Mapping(target = "contractTypeName", source = "contractType.name")
    ContractResponse toContractResponse(Contract contract);

}
