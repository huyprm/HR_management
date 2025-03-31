package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.DepartmentRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.DepartmentResponse;
import org.ptithcm2021.hr_management.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/create")
    public ApiResponse<DepartmentResponse> createDepartment(@RequestBody @Valid DepartmentRequest departmentRequest){
        return ApiResponse.<DepartmentResponse>builder()
                .data(departmentService.createDepartment(departmentRequest)).build();
    }

    @PostMapping("/update/{id}")
    public ApiResponse<DepartmentResponse> updateDepartment(@RequestBody @Valid DepartmentRequest departmentRequest,
                                                            @PathVariable String id){
        return ApiResponse.<DepartmentResponse>builder()
                .data(departmentService.updateDepartment(id, departmentRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDepartment(@PathVariable String id){
        departmentService.deleteDepartment(id);

        return ApiResponse.<Void>builder()
                .message("Deleted").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DepartmentResponse> getDepartmentById(@PathVariable String id){
        return ApiResponse.<DepartmentResponse>builder()
                .data(departmentService.getDepartment(id)).build();
    }

    @GetMapping("")
    public ApiResponse<List<DepartmentResponse>> getAllDepartment(){
        return ApiResponse.<List<DepartmentResponse>>builder()
                .data(departmentService.getDepartments()).build();
    }
}
