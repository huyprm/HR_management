package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "decisions")
public class Decision {
    @Id
    private String id;

    private String attachment;
    private String content;
    private double value;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    private DecisionEnum type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signerId")
    private User signer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="salaryPromotionId")
    private SalaryPromotion salaryPromotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionId")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seniorityAllowanceRuleId")
    private SeniorityAllowanceRule seniorityAllowanceRule;
}
