package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LeaveBalanceRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.LeaveBalanceResponse;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.service.LeaveBalanceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leave-balances")
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;

    @PostMapping("/create")
    public ApiResponse<Void> create(@RequestBody LeaveBalanceRequest leaveBalanceRequest){
        leaveBalanceService.createLeaveBalance(leaveBalanceRequest);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<LeaveBalanceResponse> getLeaveBalance (@PathVariable long userId){
        return ApiResponse.<LeaveBalanceResponse>builder()
                .data(leaveBalanceService.getLeaveBalance(userId)).build();
    }
}
