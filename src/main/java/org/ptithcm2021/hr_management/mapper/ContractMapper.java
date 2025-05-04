package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.model.Contract;

@Mapper(componentModel = "spring", uses = {UserSummaryMapper.class})
public interface ContractMapper {
    Contract toContract(ContractRequest contractRequest);

    @Mapping(target = "positionName", source = "position.name")
    @Mapping(target = "departmentName", source = "position.department.name")
    @Mapping(target = "jobGradeCoefficient", source = "jobGrade.coefficient")
    @Mapping(target = "contractTypeName", source = "contractType.name")
    ContractResponse toContractResponse(Contract contract);

}
