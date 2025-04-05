package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import org.ptithcm2021.hr_management.dto.request.AssignmentRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.AssignmentResponse;
import org.ptithcm2021.hr_management.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discipline-assignments")
public class DisciplineAssignmentController {
    private final AssignmentService assignmentService;

    @Autowired
     public DisciplineAssignmentController(@Qualifier("disciplineAssignmentServiceImpl") AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }
    @PostMapping("/create")
    public ApiResponse<AssignmentResponse> createAssignment (@RequestBody @Valid AssignmentRequest assignmentRequest){
        return ApiResponse.<AssignmentResponse>builder()
                .data(assignmentService.createAssignment(assignmentRequest)).build();
    }

    @PutMapping()
    public ApiResponse<AssignmentResponse> updateAssignment (@RequestBody @Valid AssignmentRequest assignmentRequest,
                                                             @RequestParam String rewardId, @RequestParam long userId){
        return ApiResponse.<AssignmentResponse>builder()
                .data(assignmentService.updateAssignment(assignmentRequest, rewardId, userId)).build();
    }

    @GetMapping()
    public ApiResponse<AssignmentResponse> getAssignment (@RequestParam String rewardId, @RequestParam long userId){
        return ApiResponse.<AssignmentResponse>builder()
                .data(assignmentService.getAssignment(rewardId, userId)).build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<AssignmentResponse>> getAllAssignmentByUser (@PathVariable long userId){
        return ApiResponse.<List<AssignmentResponse>>builder()
                .data(assignmentService.getAllAssignmentByUser(userId)).build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deleteAssignment(@RequestParam String rewardId, @RequestParam long userId){
        return ApiResponse.<Void>builder()
                .message("Deleted successfully").build();
    }
}
