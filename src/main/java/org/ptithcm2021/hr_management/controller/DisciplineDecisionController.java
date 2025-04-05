package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
import org.ptithcm2021.hr_management.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/api/discipline-decisions")
public class DisciplineDecisionController {
    private final DecisionService decisionService;

    @Autowired
    public DisciplineDecisionController(@Qualifier("disciplineDecisionServiceImpl") DecisionService decisionService) {
        this.decisionService = decisionService;
    }

    @PostMapping("/create")
    public ApiResponse<DecisionResponse> createDisciplineDecision(@RequestBody @Valid DecisionRequest decisionRequest) {
        return ApiResponse.<DecisionResponse>builder()
                .data(decisionService.createDecision(decisionRequest)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<DecisionResponse> updateDisciplineDecision(@PathVariable String id, @RequestBody @Valid DecisionRequest decisionRequest) {
        return ApiResponse.<DecisionResponse>builder()
                .data(decisionService.updateDecision(id, decisionRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDisciplineDecision(@PathVariable String id) {
        decisionService.deleteDecision(id);
        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DecisionResponse> getDisciplineDecision(@PathVariable String id) {
        return ApiResponse.<DecisionResponse>builder()
                .data(decisionService.getDecision(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<DecisionResponse>> getAllDisciplineDecisions() {
        return ApiResponse.<List<DecisionResponse>>builder()
                .data(decisionService.getAllDecision()).build();
    }
}
