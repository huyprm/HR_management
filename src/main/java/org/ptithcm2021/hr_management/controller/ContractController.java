package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.service.ContractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
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
                                                      @RequestParam String clause) throws Exception {

        return ApiResponse.<ContractResponse>builder()
                .data(contractService.signContract(contractId, clause))
                .build();
    }


    @GetMapping("/{contractId}")
    public ApiResponse<ContractResponse> getContract(@PathVariable int contractId) {
        return ApiResponse.<ContractResponse>builder()
                .data(contractService.getContract(contractId))
                .build();
    }

    @GetMapping
    public ApiResponse<PagedModel<ContractResponse>> getAllContract(@RequestParam(required = false) ContractStatusEnum contractStatusEnum,
                                                              @RequestParam (defaultValue = "10") int pageSize,
                                                              @RequestParam (defaultValue = "0") int pageNumber) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        return ApiResponse.<PagedModel<ContractResponse>>builder()
                .data(contractService.getAllContract(contractStatusEnum, pageable))
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


    @PutMapping("/update/{id}")
    public ApiResponse<ContractResponse> updateContract(@RequestBody ContractRequest contractRequest,
                                                        @PathVariable int id) throws Exception {
        return ApiResponse.<ContractResponse>builder().data(contractService.updateContract(id, contractRequest)).build();
    }

    @GetMapping("/current-contract")
    public ApiResponse<ContractResponse> getCurrentContract(@RequestParam long userId){
        return ApiResponse.<ContractResponse>builder()
                .data(contractService.getContractIsActiveByUser(userId)).build();
    }

    @GetMapping("/history-contract")
    public ApiResponse<List<ContractResponse>> getHistoryContract(@RequestParam long userId){
        return ApiResponse.<List<ContractResponse>>builder()
                .data(contractService.getContractsByUserIdAndStatusNotActive(userId)).build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PagedModel<ContractResponse>> getAllContractByUser(@PathVariable long userId,
                                                                          @RequestParam(required = false) ContractStatusEnum contractStatusEnum,
                                                                          @RequestParam (defaultValue = "10") int pageSize,
                                                                          @RequestParam (defaultValue = "0") int pageNumber) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return ApiResponse.<PagedModel<ContractResponse>>builder()
                .data(contractService.getAllContractByUser(userId, contractStatusEnum, pageable))
                .build();
    }
}
