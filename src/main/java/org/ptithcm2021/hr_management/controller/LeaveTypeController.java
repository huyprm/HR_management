package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LeaveTypeRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.LeaveTypeResponse;
import org.ptithcm2021.hr_management.service.LeaveTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leave-types")
public class LeaveTypeController {
    private final LeaveTypeService leaveTypeService;

    @PostMapping("/create")
    public ApiResponse<LeaveTypeResponse> createLeaveType(@RequestBody @Valid LeaveTypeRequest leaveTypeRequest){
        return ApiResponse.<LeaveTypeResponse>builder()
                .data(leaveTypeService.createLeaveType(leaveTypeRequest)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<LeaveTypeResponse> updateLeaveType(@PathVariable int id, @RequestBody @Valid LeaveTypeRequest leaveTypeRequest){
        return ApiResponse.<LeaveTypeResponse>builder()
                .data(leaveTypeService.updateLeaveType(leaveTypeRequest, id)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<LeaveTypeResponse> getLeaveType(@PathVariable int id){
        return ApiResponse.<LeaveTypeResponse>builder()
                .data(leaveTypeService.getLeaveType(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<LeaveTypeResponse>> getAllLeaveTypes(){
        return ApiResponse.<List<LeaveTypeResponse>>builder()
                .data(leaveTypeService.getAllLeaveType()).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteLeaveTypes(@PathVariable int id){
        leaveTypeService.deleteLeaveType(id);
        return ApiResponse.<String>builder().message("Deleted successfully").build();
    }
}
