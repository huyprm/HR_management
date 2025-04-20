package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "contracts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contract{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    private double basicSalary;
    private String clause;

    @Enumerated(EnumType.STRING)
    private ContractStatusEnum contractStatusEnum = ContractStatusEnum.PENDING;

    @ManyToOne()
    @JoinColumn(name = "contractTypeId")
    private ContractType contractType ;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "signerId")
    private User signer;

    @ManyToOne()
    @JoinColumn(name = "positionId")
    private Position position;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "jobGradeId")
    private JobGrade jobGrade;
}
