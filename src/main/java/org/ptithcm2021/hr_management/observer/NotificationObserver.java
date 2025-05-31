package org.ptithcm2021.hr_management.observer;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.ptithcm2021.hr_management.model.User;

public interface NotificationObserver {
    void update(User receiver, NotificationResponse notification) throws FirebaseMessagingException;
}
