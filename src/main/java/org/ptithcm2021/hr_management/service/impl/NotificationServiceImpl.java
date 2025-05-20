package org.ptithcm2021.hr_management.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.nimbusds.jose.JWEObjectJSON;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.dto.response.NotificationRecipientResponse;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.ptithcm2021.hr_management.enums.NotificationEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.NotificationMapper;
import org.ptithcm2021.hr_management.model.Department;
import org.ptithcm2021.hr_management.model.Notification;
import org.ptithcm2021.hr_management.model.NotificationRecipient;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.*;
import org.ptithcm2021.hr_management.service.NotificationService;
import org.ptithcm2021.hr_management.service.UserService;
import org.ptithcm2021.hr_management.websocket.NotificationWebSocketSender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRecipientRepository recipientRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    //private final SimpMessagingTemplate messagingTemplate;

    @Override
    public NotificationResponse createNotification(NotificationRequest notificationRequest) throws FirebaseMessagingException {
        User sender =null;
        if (notificationRequest.getUserId() !=null) {
            sender = userService.getUserToUser(notificationRequest.getUserId());
        }
        Notification notification = notificationMapper.toNotification(notificationRequest);

        List<NotificationRecipient> notificationRecipients = new ArrayList<>();

        List<User> receivers = new ArrayList<>();

        if (notificationRequest.getReceiverIds() != null){
            notificationRequest.getReceiverIds().forEach(aLong -> {
                User user = userService.getUserToUser(aLong);
                receivers.add(user);
            });
            notification.setNotificationEnum(NotificationEnum.SINGLE);
        }
        if (notificationRequest.getDepartmentIds() != null){
            notificationRequest.getDepartmentIds().forEach(s -> {
                List<User> users = userRepository.findAllByDepartmentId(s, UserStatusEnum.ACTIVE);
                receivers.addAll(users);
            });

            notification.setNotificationEnum(NotificationEnum.DEPARTMENT);
        }

        if (notificationRequest.getPositionIds() !=null){

        }

        if (notificationRequest.getReceiverIds() == null &&
            notificationRequest.getPositionIds() == null &&
            notificationRequest.getDepartmentIds() == null ){

            List<User> list = userRepository.findAll().stream()
                    .filter(user -> user.getStatus().equals(UserStatusEnum.PENDING)).toList();

            receivers.addAll(list);
            notification.setNotificationEnum(NotificationEnum.ALL);
        }

        List<User> receiversFinal  = receivers.stream().distinct().toList();

        notification.setSender(sender);
        Notification noti = notificationRepository.save(notification);

        NotificationResponse response = notificationMapper.toNotificationResponse(noti);
        for (User receiver: receiversFinal){
            NotificationRecipient recipient = NotificationRecipient.builder()
                    .notification(notification)
                    .user(receiver)
                    .build();
            notificationRecipients.add(recipient);

            sendNotificationWithWS(receiver.getId(), response);
            sendNotificationWithFireBase(receiver.getAccount().getDeviceToken(), response.getTitle());
        }

        recipientRepository.saveAll(notificationRecipients);

        return response;
    }

    @Override
    public NotificationResponse getNotification(long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        return notificationMapper.toNotificationResponse(notification);
    }

    @Override
    public PagedModel<NotificationRecipientResponse> getListNotificationRecipient(long userId, Pageable pageable) {
        Page<NotificationRecipient> notificationRecipients = recipientRepository.findAllByUserId(userId, pageable);

        return new PagedModel<>(notificationRecipients
                .map(notificationRecipient -> {
            return NotificationRecipientResponse.builder()
                    .id(notificationRecipient.getId())
                    .title(notificationRecipient.getNotification().getTitle())
                    .readStatus(notificationRecipient.isReadStatus())
                    .sendDate(notificationRecipient.getNotification().getSendDate()).build();
        }));
    }


    @Override
    public PagedModel<NotificationResponse> getListNotification(long senderId, Pageable pageable, NotificationEnum type) {
        if (type == NotificationEnum.ALL){
            return new PagedModel<>(notificationRepository.findAllNotificationIdBySenderId(senderId, pageable)
                    .map(notificationMapper::toNotificationResponse));
        }
        return new PagedModel<>(notificationRepository.findAllNotificationIdBySenderIdAndType(senderId, type, pageable)
                .map(notificationMapper::toNotificationResponse));
    }

    @Override
    public NotificationResponse getNotificationByRecipient(long id) {
        NotificationRecipient recipient = recipientRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        Notification notification = recipient.getNotification();

        return notificationMapper.toNotificationResponse(notification);
    }

    @Override
    public void maskAsSeen(List<Long> ids) {
        ids.forEach(id -> {
            NotificationRecipient recipient = recipientRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

            recipient.setReadStatus(true);
            recipientRepository.save(recipient);
        });
    }

    private void sendNotificationWithWS(long userId, NotificationResponse notification) {
        String destination = "/topic/user/" + userId;
        //messagingTemplate.convertAndSend(destination, notification);
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
