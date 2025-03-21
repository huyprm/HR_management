package org.ptithcm2021.hr_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Date;
@Entity(name = "disciplineDecisions")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplineDecision {
    @Id
    private String id;

    private String type;
    private String content;
    private Date date;

    @OneToOne
    @JoinColumn(name = "userId")
    private User signer;
}
