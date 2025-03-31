package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.ptithcm2021.hr_management.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationRequest notificationRequest);

    @Mapping(target = "sender.id", source = "sender.id")
    @Mapping(target = "sender.fullName", source = "sender.fullName")
    NotificationResponse toNotificationResponse(Notification notification);
}
