package org.ptithcm2021.hr_management.observer;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.stereotype.Component;

@Component
public class FirebaseNotificationObserver implements NotificationObserver {
    @Override
    public void update(User receiver, NotificationResponse notification) throws FirebaseMessagingException {
        if(receiver.getAccount().getDeviceToken() != null) {
            sendNotificationWithFireBase(receiver.getAccount().getDeviceToken(), notification.getTitle());
        }
    }
    private void sendNotificationWithFireBase(String deviceToken, String title) throws FirebaseMessagingException {

        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(title)
                        .build())
                .build();

        FirebaseMessaging.getInstance().send(message);
    }
}
