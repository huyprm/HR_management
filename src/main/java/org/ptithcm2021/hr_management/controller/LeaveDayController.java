package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LeaveDayRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.LeaveDayResponse;
import org.ptithcm2021.hr_management.service.LeaveDayService;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@RestController
@RequestMapping("/api/leave-days")
@RequiredArgsConstructor
public class LeaveDayController {

    private final LeaveDayService leaveDayService;

    @PostMapping("/create")
    public ApiResponse<LeaveDayResponse> createLeaveDay(@RequestBody @Valid LeaveDayRequest leaveDayRequest) {
        return ApiResponse.<LeaveDayResponse>builder()
                .data(leaveDayService.createLeaveDay(leaveDayRequest))
                .build();
    }

    @PutMapping("/{leaveDayId}")
    public ApiResponse<LeaveDayResponse> updateLeaveDay(@PathVariable int leaveDayId,
                                                        @RequestBody @Valid LeaveDayRequest leaveDayRequest) {
        return ApiResponse.<LeaveDayResponse>builder()
                .data(leaveDayService.updateLeaveDay(leaveDayRequest, leaveDayId))
                .build();
    }

    @DeleteMapping("/{leaveDayId}")
    public ApiResponse<Void> deleteLeaveDay(@PathVariable int leaveDayId) {
        leaveDayService.deleteLeaveDay(leaveDayId);
        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/{leaveDayId}")
    public ApiResponse<LeaveDayResponse> getLeaveDay(@PathVariable int leaveDayId) {
        return ApiResponse.<LeaveDayResponse>builder()
                .data(leaveDayService.getLeaveDay(leaveDayId))
                .build();
    }

    @GetMapping("/by-month")
    public ApiResponse<List<LeaveDayResponse>> getLeaveDaysByMonth(@RequestParam String yearMonth) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.MONTH_OF_YEAR)
                .appendLiteral('-')
                .appendValue(ChronoField.YEAR)
                .toFormatter();

        YearMonth parsedYearMonth = YearMonth.parse(yearMonth, formatter);
        return ApiResponse.<List<LeaveDayResponse>>builder()
                .data(leaveDayService.getListLeaveDayByMonth(parsedYearMonth))
                .build();
    }
}
