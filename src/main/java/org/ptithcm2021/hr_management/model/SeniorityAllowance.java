package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity(name = "seniorityAllowance")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeniorityAllowance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date hireDate;
    private Date calculatedDate;
    private int serviceDuration;
    private double seniorityAmount;

    @ManyToOne
    @JoinColumn(name = "seniorityAllowanceRuleId")
    private SeniorityAllowanceRule seniorityAllowanceRule;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
