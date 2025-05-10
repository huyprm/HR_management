package org.ptithcm2021.hr_management.websocket;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;

@Component
@RequiredArgsConstructor
public class NotificationWebSocketSender {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(long userId, NotificationResponse notification) {
        String destination = "/topic/user/" + userId;
        messagingTemplate.convertAndSend(destination, notification);
    }
}
