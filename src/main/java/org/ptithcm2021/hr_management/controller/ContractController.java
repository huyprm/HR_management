package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.service.ContractService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/draft")
    public ApiResponse<ContractResponse> createDraftContract(@RequestBody @Valid ContractRequest contractRequest) throws Exception {
        return ApiResponse.<ContractResponse>builder()
                .data(contractService.createDraftContract(contractRequest))
                .build();
    }

    @PutMapping("/{contractId}/sign")
    public ApiResponse<ContractResponse> signContract(@PathVariable int contractId,
                                                      @RequestParam String clause,
                                                      @RequestParam boolean isExtend) throws Exception {

        return ApiResponse.<ContractResponse>builder()
                .data(contractService.signContract(contractId, clause, isExtend))
                .build();
    }


    @GetMapping("/{contractId}")
    public ApiResponse<ContractResponse> getContract(@PathVariable int contractId) {
        return ApiResponse.<ContractResponse>builder()
                .data(contractService.getContract(contractId))
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ContractResponse>> getAllContractByUser(@PathVariable long userId,
                                                                    @RequestParam(required = false) ContractStatusEnum contractStatusEnum) {
        return ApiResponse.<List<ContractResponse>>builder()
                .data(contractService.getAllContractByUser(userId, contractStatusEnum))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ContractResponse>> getAllContract(@RequestParam(required = false) ContractStatusEnum contractStatusEnum) {
        return ApiResponse.<List<ContractResponse>>builder()
                .data(contractService.getAllContract(contractStatusEnum))
                .build();
    }

    @DeleteMapping("/{contractId}")
    public ApiResponse<Void> deleteContract(@PathVariable int contractId) throws Exception {
        contractService.deleteContract(contractId);
        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }


    @PostMapping("/extend/{contractId}")
    public ApiResponse<ContractResponse> extendContract(@PathVariable int contractId,
                                                        @RequestBody @Valid ContractRequest contractRequest) throws Exception {
        return ApiResponse.<ContractResponse>builder()
                .data(contractService.extendContract(contractId, contractRequest))
                .build();
    }

}
