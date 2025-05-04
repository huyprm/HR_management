package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;
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
@Builder
public class Contract{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate startDate;

    private LocalDate endDate;

    private double basicSalary;
    private String clause;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ContractStatusEnum contractStatusEnum = ContractStatusEnum.PENDING;

    @ManyToOne()
    @JoinColumn(name = "contractTypeId")
    private ContractType contractType ;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "signerId")
    private User signer;

    @ManyToOne()
    @JoinColumn(name = "positionId")
    private Position position;

    @ManyToOne()
    @JoinColumn(name = "jobGradeId")
    private JobGrade jobGrade;
}
