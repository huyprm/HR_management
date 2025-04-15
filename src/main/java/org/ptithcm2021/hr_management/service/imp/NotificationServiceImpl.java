package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.dto.response.NotificationRecipientResponse;
import org.ptithcm2021.hr_management.dto.response.NotificationResponse;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRecipientRepository recipientRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserService userService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationResponse createNotification(NotificationRequest notificationRequest) {
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
            notification.setInfoReceiver("Personal");
        }
//        if (notificationRequest.getDepartmentIs() != null){
//            notificationRequest.getDepartmentIs().forEach(s -> {
//                Department department = departmentRepository.findById(s)
//                        .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
//
//                receivers.addAll(department.getUsers().stream()
//                        .filter(user -> user.getStatus().equals(UserStatusEnum.PENDING)).toList());
//            });
//            notification.setInfoReceiver("Department");
//        }

        if (notificationRequest.getPositionIs() !=null){

        }

        if (notificationRequest.getReceiverIds() == null &&
            notificationRequest.getPositionIs() == null &&
            notificationRequest.getDepartmentIs() == null ){

            List<User> list = userRepository.findAll().stream()
                    .filter(user -> user.getStatus().equals(UserStatusEnum.PENDING)).toList();

            receivers.addAll(list);
            notification.setInfoReceiver("All");
        }

        List<User> receiversFinal  = receivers.stream().distinct().toList();

        for (User receiver: receiversFinal){
            NotificationRecipient recipient = NotificationRecipient.builder()
                    .notification(notification)
                    .user(receiver)
                    .build();
            notificationRecipients.add(recipient);
        }

        notification.setSender(sender);
        Notification noti = notificationRepository.save(notification);
        recipientRepository.saveAll(notificationRecipients);

        return notificationMapper.toNotificationResponse(noti);
    }

    @Override
    public NotificationResponse getNotification(long id) {
        NotificationRecipient recipient = recipientRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if(!recipient.isReadStatus()) {
            recipient.setReadStatus(true);
            recipientRepository.save(recipient);
        }

        return notificationMapper.toNotificationResponse(recipient.getNotification());
    }

    @Override
    public List<NotificationRecipientResponse> getAllNotificationRecipient(long userId) {
        List<NotificationRecipient> notificationRecipients = recipientRepository.finAllByUserId(userId);

        return notificationRecipients
                .stream().map(notificationRecipient -> {
            return NotificationRecipientResponse.builder()
                    .id(notificationRecipient.getId())
                    .title(notificationRecipient.getNotification().getTitle())
                    .readStatus(notificationRecipient.isReadStatus())
                    .sendDate(notificationRecipient.getNotification().getSendDate()).build();
        }).toList();
    }

    @Override
    public List<NotificationRecipientResponse> getTop5NotificationRecipient(long userId) {
        Pageable pageable = PageRequest.of(0,5);
        List<NotificationRecipient> notificationRecipients = recipientRepository.findTop5ByUserId(userId, pageable);

        return notificationRecipients
                .stream().map(notificationRecipient -> {
            return NotificationRecipientResponse.builder()
                    .id(notificationRecipient.getId())
                    .title(notificationRecipient.getNotification().getTitle())
                    .readStatus(notificationRecipient.isReadStatus())
                    .sendDate(notificationRecipient.getNotification().getSendDate()).build();
        }).toList();
    }
}
