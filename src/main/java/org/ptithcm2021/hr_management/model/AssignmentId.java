package org.ptithcm2021.hr_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentId implements Serializable {
    @Column(name = "userId")
    private long userId;

    @Column(name = "rewardDecisionId")
    private String decisionId;
}
