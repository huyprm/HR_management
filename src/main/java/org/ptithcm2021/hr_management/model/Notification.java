package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "notifications")
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
    private String attached;

    @Column
    private LocalDateTime sendDate = LocalDateTime.now();

    private String infoReceiver;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User sender;
}
