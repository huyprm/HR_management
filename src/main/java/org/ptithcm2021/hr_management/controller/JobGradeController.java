package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.JobGradeRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.JobGradeResponse;
import org.ptithcm2021.hr_management.service.JobGradeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-grades")
public class JobGradeController {
    private final JobGradeService jobGradeService;

    @PostMapping("/create")
    public ApiResponse<JobGradeResponse> createJobGrade(@RequestBody @Valid JobGradeRequest jobGradeRequest) {
        return ApiResponse.<JobGradeResponse>builder()
                .data(jobGradeService.createJobGrade(jobGradeRequest)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<JobGradeResponse> updateJobGrade(@PathVariable String id,
                                                        @RequestBody @Valid JobGradeRequest jobGradeRequest) {
        return ApiResponse.<JobGradeResponse>builder()
                .data(jobGradeService.updateJobGrade(id, jobGradeRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteJobGrade(@PathVariable String id) {
        jobGradeService.deleteJobGrade(id);
        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<JobGradeResponse> getJobGrade(@PathVariable String id) {
        return ApiResponse.<JobGradeResponse>builder()
                .data(jobGradeService.getJobGrade(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<JobGradeResponse>> getJobGrades() {
        return ApiResponse.<List<JobGradeResponse>>builder()
                .data(jobGradeService.getAllJobGrade()).build();
    }
}
