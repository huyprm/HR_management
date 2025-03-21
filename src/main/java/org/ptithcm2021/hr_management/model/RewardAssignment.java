package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "rewardAssignments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name ="rewardDecisionId")
    private RewardDecision rewardDecision;

    @ManyToOne
    @JoinColumn(name ="userId")
    private User user;
}
