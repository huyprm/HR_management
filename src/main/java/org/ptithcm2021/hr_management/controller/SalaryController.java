//package org.ptithcm2021.hr_management.controller;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.ptithcm2021.hr_management.dto.request.SalaryRequest;
//import org.ptithcm2021.hr_management.dto.response.ApiResponse;
//import org.ptithcm2021.hr_management.dto.response.SalaryResponse;
//import org.ptithcm2021.hr_management.service.SalaryService;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.YearMonth;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/salaries")
//@RequiredArgsConstructor
//public class SalaryController {
//    private final SalaryService salaryService;
//
//    @PostMapping("/create")
//    public ApiResponse<SalaryResponse> createSalary(@RequestBody @Valid SalaryRequest request) {
//        return ApiResponse.<SalaryResponse>builder()
//                .data(salaryService.createSalary(request)).build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteSalary(@PathVariable int id) {
//        salaryService.deleteSalary(id);
//        return ApiResponse.<Void>builder().message("Deleted successfully").build();
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<SalaryResponse> getSalary(@PathVariable int id) {
//        return ApiResponse.<SalaryResponse>builder()
//                .data(salaryService.getSalary(id)).build();
//    }
//
//    @GetMapping("/user/{userId}")
//    public ApiResponse<List<SalaryResponse>> getSalariesByUser(@PathVariable long userId) {
//        return ApiResponse.<List<SalaryResponse>>builder()
//                .data(salaryService.getSalariesByUser(userId)).build();
//    }
//
//    @GetMapping("/month")
//    public ApiResponse<List<SalaryResponse>> getSalariesByMonth(
//            @RequestParam int year,
//            @RequestParam int month) {
//        YearMonth yearMonth = YearMonth.of(year, month);
//        return ApiResponse.<List<SalaryResponse>>builder()
//                .data(salaryService.getSalariesByMonth(yearMonth)).build();
//    }
//
//    @PostMapping("/generate-monthly")
//    public ApiResponse<Void> generateMonthlySalaries(
//            @RequestParam int year,
//            @RequestParam int month) {
//        YearMonth yearMonth = YearMonth.of(year, month);
//        salaryService.generateMonthlySalaries(yearMonth);
//        return ApiResponse.<Void>builder()
//                .message("Salaries generated for " + yearMonth.format(DateTimeFormatter.ofPattern("MM/yyyy")))
//                .build();
//    }
//
//    @GetMapping
//    public ApiResponse<List<SalaryResponse>> getAllSalaries() {
//        return ApiResponse.<List<SalaryResponse>>builder()
//                .data(salaryService.getAllSalaries()).build();
//    }
//}