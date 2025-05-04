package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LeaveBalanceRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.LeaveBalanceResponse;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.service.LeaveBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leave-balances")
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;

    @GetMapping("/{userId}")
    public ApiResponse<LeaveBalanceResponse> getLeaveBalance(@PathVariable long userId) {
        return ApiResponse.<LeaveBalanceResponse>builder()
                .data(leaveBalanceService.getLeaveBalance(userId)).build();
    }
    
    // Endpoint mới để lấy thông tin ngày nghỉ phép theo tháng cụ thể
    @GetMapping("/{userId}/{year}/{month}")
    public ApiResponse<LeaveBalanceResponse> getLeaveBalanceByMonth(
            @PathVariable long userId,
            @PathVariable int year,
            @PathVariable int month) {
        return ApiResponse.<LeaveBalanceResponse>builder()
                .data(leaveBalanceService.getLeaveBalanceByMonth(userId, year, month)).build();
    }
    
    // Endpoint để lấy tất cả thông tin ngày nghỉ phép trong năm (theo tháng)
    @GetMapping("/{userId}/{year}")
    public ApiResponse<List<LeaveBalanceResponse>> getAllLeaveBalancesForYear(
            @PathVariable long userId,
            @PathVariable int year) {
                
        return ApiResponse.<List<LeaveBalanceResponse>>builder()
                .data(leaveBalanceService.getAllLeaveBalancesByYear(userId, year)).build();
    }

}
