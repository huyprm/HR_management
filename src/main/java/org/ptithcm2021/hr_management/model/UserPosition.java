package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Entity(name = "userPositions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "postionId")
    private Position position;

    @ManyToOne
    @JoinColumn(name ="userId")
    private User user;
}
