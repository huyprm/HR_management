package org.ptithcm2021.hr_management.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.dto.request.LeaveApplicationRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.LeaveApplicationResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.service.LeaveApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/leave-applications")
@RequiredArgsConstructor
@Tag(name = "Leave Application Controller")
public class LeaveApplicationController {
    private final LeaveApplicationService leaveApplicationService;

    @PostMapping("/create")
    public ApiResponse<LeaveApplicationResponse> createApplication(@RequestBody @Valid LeaveApplicationRequest leaveApplicationRequest) {
        String au = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(au);
        return ApiResponse.<LeaveApplicationResponse>builder()
                .data(leaveApplicationService.createApplication(leaveApplicationRequest)).build();
    }

    @PostMapping("/confirm/{applicationId}")
    public ApiResponse<LeaveApplicationResponse> confirmApplication(@PathVariable long applicationId,
                                                                    @RequestParam FormStatusEnum formStatusEnum) {
        return ApiResponse.<LeaveApplicationResponse>builder()
                .data(leaveApplicationService.confirmApplication(formStatusEnum, applicationId)).build();
    }


    @GetMapping()
    public ApiResponse<List<LeaveApplicationResponse>> getApplicationIsPending(@RequestParam(required = false) FormStatusEnum formStatusEnum) {
        return ApiResponse.<List<LeaveApplicationResponse>>builder()
                .data(leaveApplicationService.getApplicationIsPending(formStatusEnum)).build();
    }

    @GetMapping("/{applicationId}")
    public ApiResponse<LeaveApplicationResponse> getApplication(@PathVariable long applicationId) {
        return ApiResponse.<LeaveApplicationResponse>builder()
                .data(leaveApplicationService.getApplication(applicationId)).build();
    }
}

