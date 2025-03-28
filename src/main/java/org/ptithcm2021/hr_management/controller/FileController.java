package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<String> uploadImage(@RequestParam MultipartFile file) throws IOException {
        return ApiResponse.<String>builder().data(fileService.uploadImage(file)).build();
    }

    @PostMapping("/edit")
    public ApiResponse<String> editImage(@RequestParam MultipartFile file, @RequestParam String currentImg) throws Exception {
        return ApiResponse.<String>builder().data(fileService.editImage(file, currentImg)).build();
    }
}
