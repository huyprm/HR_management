package org.ptithcm2021.hr_management.observer;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketNotificationObserver implements NotificationObserver {
    private final SimpMessagingTemplate messagingTemplate;

    public void update(User user, NotificationResponse notification) {
        String destination = "/topic/user/" + user.getId();
        messagingTemplate.convertAndSend(destination, notification);
    }
}
