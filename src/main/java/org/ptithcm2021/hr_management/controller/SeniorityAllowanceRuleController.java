package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.SeniorityAllowanceRuleRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.SeniorityAllowanceRuleResponse;
import org.ptithcm2021.hr_management.enums.FetchStatus;
import org.ptithcm2021.hr_management.service.SeniorityAllowanceRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seniority-allowance-rules")
@RequiredArgsConstructor
public class SeniorityAllowanceRuleController {

    private final SeniorityAllowanceRuleService seniorityAllowanceRuleService;

    @PostMapping("/create")
    public ApiResponse<SeniorityAllowanceRuleResponse> createAllowanceRule(@RequestBody @Valid SeniorityAllowanceRuleRequest request) {
        return ApiResponse.<SeniorityAllowanceRuleResponse>builder()
                .data(seniorityAllowanceRuleService.createAllowanceRule(request))
                .build();
    }

    @PutMapping("/{ruleId}")
    public ApiResponse<SeniorityAllowanceRuleResponse> updateAllowanceRule(@PathVariable int ruleId,
                                                                           @RequestBody @Valid SeniorityAllowanceRuleRequest request) {
        return ApiResponse.<SeniorityAllowanceRuleResponse>builder()
                .data(seniorityAllowanceRuleService.updateAllowanceRule(request, ruleId))
                .build();
    }

    @DeleteMapping("/{ruleId}")
    public ApiResponse<Void> deleteAllowanceRule(@PathVariable int ruleId) {
        seniorityAllowanceRuleService.deleteAllowanceRule(ruleId);
        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/{ruleId}")
    public ApiResponse<SeniorityAllowanceRuleResponse> getAllowanceRule(@PathVariable int ruleId) {
        return ApiResponse.<SeniorityAllowanceRuleResponse>builder()
                .data(seniorityAllowanceRuleService.getAllowanceRule(ruleId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SeniorityAllowanceRuleResponse>> getAllAllowanceRules(@RequestParam FetchStatus fetchStatus) {
        return ApiResponse.<List<SeniorityAllowanceRuleResponse>>builder()
                .data(seniorityAllowanceRuleService.getAllAllowanceRule(fetchStatus))
                .build();
    }
}

