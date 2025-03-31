package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.RewardAssignmentRequest;
import org.ptithcm2021.hr_management.dto.request.RewardDecisionRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.model.RewardAssignment;
import org.ptithcm2021.hr_management.service.RewardAssignmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reward_assigment")
public class RewardAssigmentController {
    private final RewardAssignmentService rewardAssignmentService;
    @PostMapping("/create")
    public ApiResponse<RewardAssignment> createAssigment (@RequestParam String rewardId, @RequestParam long userid){
        return ApiResponse.<RewardAssignment>builder().data(rewardAssignmentService.createRewardAssignment(rewardId, userid)).build();
    }

    @PostMapping("/update")
    public ApiResponse<RewardAssignment> updateAssigment (@RequestBody RewardAssignmentRequest rewardAssignmentRequest,
                                                          @RequestParam String rewardId, @RequestParam long userid){
        return ApiResponse.<RewardAssignment>builder()
                .data(rewardAssignmentService.updateRewardAssignment(rewardAssignmentRequest, rewardId, userid)).build();
    }

    @GetMapping("")
    public ApiResponse<RewardAssignment> getAssigment (@RequestParam String rewardId, @RequestParam long userid){
        return ApiResponse.<RewardAssignment>builder()
                .data(rewardAssignmentService.getRewardAssignment(rewardId, userid)).build();
    }
}
