package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.dto.response.NotificationRecipientResponse;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.ptithcm2021.hr_management.enums.NotificationEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface NotificationService {
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER', 'SCOPE_STAFF')")
    NotificationResponse createNotification(NotificationRequest notificationRequest);

    NotificationResponse getNotification(long id);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    PagedModel<NotificationRecipientResponse> getListNotificationRecipient (long userId, Pageable pageable );

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER', 'SCOPE_STAFF')")
    PagedModel<NotificationResponse> getListNotification (long senderId, Pageable pageable, NotificationEnum type);

    NotificationResponse getNotificationByRecipient(long id);

    void maskAsSeen(List<Long> ids);
}
