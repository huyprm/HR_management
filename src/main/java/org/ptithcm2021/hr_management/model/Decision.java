package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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

    @Column(columnDefinition = "text")
    private String content;

    private double value;

    @Temporal(TemporalType.DATE)
    private LocalDate date;
    
    @Temporal(TemporalType.DATE)
    private LocalDate effectiveDate;
    
    private boolean processed = true;

    @Enumerated(EnumType.STRING)
    private DecisionEnum type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signer_id")
    private User signer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="salary_promotion_id")
    private SalaryPromotion salaryPromotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seniority_allowance_rule_id")
    private SeniorityAllowanceRule seniorityAllowanceRule;
}
