package org.ptithcm2021.hr_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.NotificationRecipientResponse;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.ptithcm2021.hr_management.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/create")
    public ApiResponse<NotificationResponse> createNotification (@RequestBody @Valid NotificationRequest notificationRequest){
        return ApiResponse.<NotificationResponse>builder()
                .data(notificationService.createNotification(notificationRequest)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<NotificationResponse> getNotification(@PathVariable long id){
        return ApiResponse.<NotificationResponse>builder()
                .data(notificationService.getNotification(id)).build();
    }

    @GetMapping("/user/{id}")
    public ApiResponse<Page<NotificationRecipientResponse>> getAllNotification(@PathVariable long id,
                                                                               @RequestParam (defaultValue = "10") int pageSize,
                                                                               @RequestParam (defaultValue = "0") int pageNumber){
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return ApiResponse.<Page<NotificationRecipientResponse>>builder().data(notificationService.getListNotificationRecipient(id, pageable)).build();
    }

    @SendTo("/user/notification")
    public String sendNotification(String message) {
        return message; // Gửi lại thông báo
    }
}
