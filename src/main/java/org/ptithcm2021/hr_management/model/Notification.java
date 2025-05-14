package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.hr_management.enums.NotificationEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notifications")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String content;

    @CollectionTable(name = "notification_attached", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "attached")
    private List<String> attached = new ArrayList<>();

    @Column
    private LocalDateTime sendDate = LocalDateTime.now();

    private String recipientText;

    @Enumerated(EnumType.STRING)
    private NotificationEnum notificationEnum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;
}
