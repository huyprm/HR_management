package org.ptithcm2021.hr_management.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.NotificationRecipientResponse;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.ptithcm2021.hr_management.enums.NotificationEnum;
import org.ptithcm2021.hr_management.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/create")
    public ApiResponse<NotificationResponse> createNotification (@RequestBody @Valid NotificationRequest notificationRequest) throws FirebaseMessagingException {
        return ApiResponse.<NotificationResponse>builder()
                .data(notificationService.createNotification(notificationRequest)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<NotificationResponse> getNotification(@PathVariable long id){
        return ApiResponse.<NotificationResponse>builder()
                .data(notificationService.getNotification(id)).build();
    }

    @GetMapping("/user/{id}")
    public ApiResponse<PagedModel<NotificationRecipientResponse>> getAllNotificationRecipient(@PathVariable long id,
                                                                                     @RequestParam (defaultValue = "10") int pageSize,
                                                                                     @RequestParam (defaultValue = "0") int pageNumber){
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return ApiResponse.<PagedModel<NotificationRecipientResponse>>builder().data(notificationService.getListNotificationRecipient(id, pageable)).build();
    }

    @GetMapping("/sender/{id}")
    public ApiResponse<PagedModel<NotificationResponse>> getAllNotification(@PathVariable long id,
                                                                            @RequestParam (defaultValue = "10") int pageSize,
                                                                            @RequestParam (defaultValue = "0") int pageNumber,
                                                                            @RequestParam (defaultValue = "ALL") NotificationEnum type){
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return ApiResponse.<PagedModel<NotificationResponse>>builder().data(notificationService.getListNotification(id, pageable, type)).build();
    }

    @GetMapping("/{id}/read")
    public ApiResponse<NotificationResponse> getNotificationRecipient(@PathVariable long id){
        return ApiResponse.<NotificationResponse>builder().data(notificationService.getNotificationByRecipient(id)).build();
    }

    @PutMapping("/seen")
    public ApiResponse<Void> markAsSeen(@RequestBody List<Long> ids){
        notificationService.maskAsSeen(ids);
        return ApiResponse.<Void>builder().build();
    }
}
