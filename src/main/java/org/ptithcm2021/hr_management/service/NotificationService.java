package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.dto.response.NotificationRecipientResponse;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface NotificationService {
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER', 'SCOPE_STAFF')")
    NotificationResponse createNotification(NotificationRequest notificationRequest);

    NotificationResponse getNotification(long id);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    List<NotificationRecipientResponse> getAllNotificationRecipient (long userId);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    List<NotificationRecipientResponse> getTop5NotificationRecipient (long userId);
}
