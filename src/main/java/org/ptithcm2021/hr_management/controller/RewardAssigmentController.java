package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.AssignmentRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.AssignmentResponse;
import org.ptithcm2021.hr_management.service.RewardAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reward-assigment")
public class RewardAssigmentController {
    private final RewardAssignmentService rewardAssignmentService;

    @PostMapping("/create")
    public ApiResponse<AssignmentResponse> createAssignment (@RequestBody AssignmentRequest assignmentRequest){
        return ApiResponse.<AssignmentResponse>builder()
                .data(rewardAssignmentService.createRewardAssignment(assignmentRequest)).build();
    }

    @PutMapping()
    public ApiResponse<AssignmentResponse> updateAssignment (@RequestBody AssignmentRequest assignmentRequest,
                                                             @RequestParam String rewardId, @RequestParam long userId){
        return ApiResponse.<AssignmentResponse>builder()
                .data(rewardAssignmentService.updateRewardAssignment(assignmentRequest, rewardId, userId)).build();
    }

    @GetMapping()
    public ApiResponse<AssignmentResponse> getAssignment (@RequestParam String rewardId, @RequestParam long userId){
        return ApiResponse.<AssignmentResponse>builder()
                .data(rewardAssignmentService.getRewardAssignment(rewardId, userId)).build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<AssignmentResponse>> getAllAssignmentByUser (@PathVariable long userId){
        return ApiResponse.<List<AssignmentResponse>>builder()
                .data(rewardAssignmentService.getAllRewardAssignmentByUser(userId)).build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deleteAssignment(@RequestParam String rewardId, @RequestParam long userId){
        return ApiResponse.<Void>builder()
                .message("Deleted successfully").build();
    }


}
