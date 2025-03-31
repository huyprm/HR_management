package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "notificationRecipients")
@Builder
public class NotificationRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Builder.Default
    private boolean readStatus = false;

    @ManyToOne
    @JoinColumn(name = "notificationId")
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
