package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.FeedbackRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.FeedbackResponse;
import org.ptithcm2021.hr_management.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
public class FeedBackController {
    private final FeedbackService feedbackService;

    @PostMapping("/create")
    public ApiResponse<FeedbackResponse> createFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        return ApiResponse.<FeedbackResponse>builder()
                .data(feedbackService.createFeedback(feedbackRequest)).build();
    }


    @GetMapping("/{id}")
    public ApiResponse<FeedbackResponse> getFeedback(@PathVariable long id) {
        return ApiResponse.<FeedbackResponse>builder()
                .data(feedbackService.getFeedback(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<FeedbackResponse>> getFeedbacks() {
        return ApiResponse.<List<FeedbackResponse>>builder()
                .data(feedbackService.getAllFeedback()).build();
    }
}
