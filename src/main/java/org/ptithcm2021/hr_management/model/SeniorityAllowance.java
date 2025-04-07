package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity(name = "seniorityAllowances")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeniorityAllowance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @Temporal(TemporalType.DATE)
    private Date calculatedDate;

    private int serviceDuration;

    @ManyToOne
    @JoinColumn(name = "seniorityAllowanceRuleId")
    private SeniorityAllowanceRule seniorityAllowanceRule;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

}
