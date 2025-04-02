package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.RewardDecisionRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.RewardDecisionResponse;
import org.ptithcm2021.hr_management.service.RewardDecisionService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reward-decisions")
public class RewardDecisionController {
    private final RewardDecisionService rewardDecisionService;

    @PostMapping("/create")
    public ApiResponse<RewardDecisionResponse> createRewardDecision(@RequestBody @Valid RewardDecisionRequest rewardDecisionRequest) {
        return ApiResponse.<RewardDecisionResponse>builder()
                .data(rewardDecisionService.createRewardDecision(rewardDecisionRequest)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<RewardDecisionResponse> updateRewardDecision(@PathVariable String id, @RequestBody @Valid RewardDecisionRequest rewardDecisionRequest) {
        return ApiResponse.<RewardDecisionResponse>builder()
                .data(rewardDecisionService.updateRewardDecision(id, rewardDecisionRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRewardDecision(@PathVariable String id) {
        rewardDecisionService.deleteRewardDecision(id);
        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RewardDecisionResponse> getRewardDecision(@PathVariable String id) {
        return ApiResponse.<RewardDecisionResponse>builder()
                .data(rewardDecisionService.getRewardDecision(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<RewardDecisionResponse>> getAllRewardDecisions() {
        return ApiResponse.<List<RewardDecisionResponse>>builder()
                .data(rewardDecisionService.getAllRewardDecision()).build();
    }
}
