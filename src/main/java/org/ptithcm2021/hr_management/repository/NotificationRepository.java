package org.ptithcm2021.hr_management.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ptithcm2021.hr_management.enums.NotificationEnum;
import org.ptithcm2021.hr_management.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository <Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.sender.id = :senderId and n.notificationEnum = :type ORDER BY n.sendDate DESC")
    Page<Notification> findAllNotificationIdBySenderIdAndType(@Param("senderId") long senderId,
                                                       @Param("type") NotificationEnum type,
                                                       Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.sender.id = :senderId ORDER BY n.sendDate DESC")
    Page<Notification> findAllNotificationIdBySenderId(@Param("senderId") long senderId,
                                                       Pageable pageable);
}
