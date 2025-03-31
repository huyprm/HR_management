package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.model.RewardAssignment;
import org.ptithcm2021.hr_management.service.RewardAssignmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reward_assigment")
public class RewardAssigmentController {
    private final RewardAssignmentService rewardAssignmentService;
    @PostMapping("/create")
    public ApiResponse<RewardAssignment> createAssigment (@RequestParam String rewardId, @RequestParam long userid){
        return ApiResponse.<RewardAssignment>builder().data(rewardAssignmentService.createRewardAssignment(rewardId, userid)).build();
    }
}
