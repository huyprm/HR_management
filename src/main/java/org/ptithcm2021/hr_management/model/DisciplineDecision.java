package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "userId")
    private User signer;
}
