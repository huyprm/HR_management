package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.ContractTypeRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.ContractTypeResponse;
import org.ptithcm2021.hr_management.service.ContractTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contract-types")
public class ContractTypeController {
    private final ContractTypeService contractTypeService;

    @PostMapping("/create")
    public ApiResponse<ContractTypeResponse> createContractType(@RequestBody @Valid ContractTypeRequest contractTypeRequest){
        return ApiResponse.<ContractTypeResponse>builder()
                .data(contractTypeService.createContractType(contractTypeRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteContractType(@PathVariable String id){
        contractTypeService.deleteContractType(id);
        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ContractTypeResponse> getContractType(@PathVariable String id){
        return ApiResponse.<ContractTypeResponse>builder()
                .data(contractTypeService.getContractType(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<ContractTypeResponse>> getAllContractType(){
        return ApiResponse.<List<ContractTypeResponse>>builder()
                .data(contractTypeService.getAllContractType()).build();
    }
}
