package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionRequest;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.SalaryPromotionResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.service.SalaryPromotionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary-promotions")
@RequiredArgsConstructor
public class SalaryPromotionController {
    private final SalaryPromotionService salaryPromotionService;

    @PostMapping("/create")
    public ApiResponse<SalaryPromotionResponse> createSalaryPromotion(@RequestBody @Valid SalaryPromotionRequest request) {
        return ApiResponse.<SalaryPromotionResponse>builder()
                .data(salaryPromotionService.createSalaryPromotion(request)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SalaryPromotionResponse> getSalaryPromotionById(@PathVariable int id) {
        return ApiResponse.<SalaryPromotionResponse>builder()
                .data(salaryPromotionService.getSalaryPromotionById(id)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SalaryPromotionResponse> updateSalaryPromotion(
            @PathVariable int id,
            @RequestBody @Valid SalaryPromotionUpdateRequest updateRequest) {
        return ApiResponse.<SalaryPromotionResponse>builder()
                .data(salaryPromotionService.updateSalaryPromotion(id, updateRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSalaryPromotion(@PathVariable int id) {
        salaryPromotionService.deleteSalaryPromotion(id);
        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<SalaryPromotionResponse>> getSalaryPromotionsByUserAndStatus(
            @PathVariable long userId,
            @RequestParam(required = false) FormStatusEnum status) {
        return ApiResponse.<List<SalaryPromotionResponse>>builder()
                .data(salaryPromotionService.getSalaryPromotionByStatusAndUserId(userId, status)).build();
    }

    @GetMapping("/signer/{signerId}")
    public ApiResponse<List<SalaryPromotionResponse>> getSalaryPromotionsBySignerAndStatus(
            @PathVariable long signerId,
            @RequestParam FormStatusEnum status) {
        return ApiResponse.<List<SalaryPromotionResponse>>builder()
                .data(salaryPromotionService.getSalaryPromotionByStatusAndSignId(signerId, status)).build();
    }

    @PutMapping("/{id}/approve")
    public ApiResponse<SalaryPromotionResponse> approveSalaryPromotion(
            @PathVariable int id,
            @RequestBody @Valid SalaryPromotionUpdateRequest updateRequest) {
        return ApiResponse.<SalaryPromotionResponse>builder()
                .data(salaryPromotionService.approveAndApplySalaryPromotion(id, updateRequest))
                .message("Promotion request processed successfully")
                .build();
    }
}
