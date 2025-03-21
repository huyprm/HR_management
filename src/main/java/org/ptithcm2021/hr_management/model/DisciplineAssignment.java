package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "disciplineAssigments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplineAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name ="userId")
    private User user;

    @ManyToOne
    @JoinColumn(name ="disciplineDecisionId")
    private DisciplineDecision disciplineDecision;
}
