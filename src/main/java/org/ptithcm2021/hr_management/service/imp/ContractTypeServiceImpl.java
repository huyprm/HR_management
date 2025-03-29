package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.ContractTypeRequest;
import org.ptithcm2021.hr_management.dto.response.ContractTypeResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.ContractTypeMapper;
import org.ptithcm2021.hr_management.model.ContractType;
import org.ptithcm2021.hr_management.repository.ContractTypeRepository;
import org.ptithcm2021.hr_management.service.ContractTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractTypeServiceImpl implements ContractTypeService {
    private final ContractTypeRepository contractTypeRepository;
    private final ContractTypeMapper contractTypeMapper;

    @Override
    public ContractTypeResponse createContractType(ContractTypeRequest contractTypeRequest) {
        if (contractTypeRepository.existsByName(contractTypeRequest.getName())) {
            throw new AppException(ErrorCode.CONTRACT_TYPE_NAME_EXISTED);
        }

        ContractType contractType = contractTypeMapper.toContractType(contractTypeRequest);

        return contractTypeMapper.toContractTypeResponse(contractTypeRepository.save(contractType));
    }

    @Override
    public ContractTypeResponse updateContractType(String id, ContractTypeRequest contractTypeRequest) {
        if (contractTypeRepository.existsByName(contractTypeRequest.getName())) {
            throw new AppException(ErrorCode.CONTRACT_TYPE_NAME_EXISTED);
        }

        ContractType contractType = contractTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_TYPE_NOT_FOUND));

        contractTypeMapper.updateContractType(contractType, contractTypeRequest);

        return contractTypeMapper.toContractTypeResponse(contractTypeRepository.save(contractType));
    }

    @Override
    public void deleteContractType(String id) {
        try{
            contractTypeRepository.deleteById(id);
        }catch (Exception e){
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }

    @Override
    public ContractTypeResponse getContractType(String id) {
        ContractType contractType = contractTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_TYPE_NOT_FOUND));

        return contractTypeMapper.toContractTypeResponse(contractType);
    }

    @Override
    public List<ContractTypeResponse> getAllContractType() {
        return contractTypeRepository.findAll().stream().map(contractTypeMapper::toContractTypeResponse).toList();
    }
}
