package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.model.SeniorityAllowance;
import org.ptithcm2021.hr_management.service.SeniorityAllowanceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seniority-allowance")
public class SeniorityAllowanceController {
    private final SeniorityAllowanceService seniorityAllowanceService;

}
