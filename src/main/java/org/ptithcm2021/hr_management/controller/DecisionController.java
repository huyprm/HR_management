package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
import org.ptithcm2021.hr_management.dto.request.DecisionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.ptithcm2021.hr_management.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/decisions")
public class DecisionController {
    private final DecisionService decisionService;

    @PostMapping("/create")
    public ApiResponse<DecisionResponse> createDecision(@RequestBody @Valid DecisionRequest decisionRequest) {
        return ApiResponse.<DecisionResponse>builder()
                .data(decisionService.createDecision(decisionRequest)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<DecisionResponse> updateDecision(@PathVariable String id, @RequestBody @Valid DecisionUpdateRequest decisionRequest) {
        return ApiResponse.<DecisionResponse>builder()
                .data(decisionService.updateDecision(id, decisionRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDecision(@PathVariable String id) {
        decisionService.deleteDecision(id);
        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DecisionResponse> getDecision(@PathVariable String id) {
        return ApiResponse.<DecisionResponse>builder()
                .data(decisionService.getDecision(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<DecisionResponse>> getAllDecisions(@RequestParam(required = false) DecisionEnum type) {
        return ApiResponse.<List<DecisionResponse>>builder()
                .data(decisionService.getAllDecisionByType(type)).build();
    }
}
