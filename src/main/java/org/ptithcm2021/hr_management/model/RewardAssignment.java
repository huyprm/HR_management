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
    @EmbeddedId
    private AssignmentId id;

    @ManyToOne
    @MapsId("decisionId")
    @JoinColumn(name ="decision_id")
    private RewardDecision rewardDecision;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name ="user_id")
    private User user;
}
