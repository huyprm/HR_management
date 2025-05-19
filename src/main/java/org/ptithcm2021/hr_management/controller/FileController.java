package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/image")
    public ApiResponse<String> uploadImage(@RequestParam MultipartFile file) throws IOException {
        return ApiResponse.<String>builder().data(fileService.uploadImage(file)).build();
    }

    @PostMapping("/edit")
    public ApiResponse<String> editImage(@RequestParam MultipartFile file, @RequestParam String currentImg) throws Exception {
        return ApiResponse.<String>builder().data(fileService.editImage(file, currentImg)).build();
    }

    @PostMapping("/pdf")
    public ApiResponse<String> uploadAuto(@RequestParam MultipartFile file) throws Exception {
        return ApiResponse.<String>builder().data(fileService.uploadFile(file)).build();
    }

    @GetMapping("/pdf")
    public ResponseEntity<InputStreamResource> streamFile(@RequestParam String fileId) throws IOException {
        InputStream inputStream = fileService.downloadFilePdf(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }
}
