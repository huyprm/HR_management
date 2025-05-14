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
    @JoinColumn(name = "contract_type_id")
    private ContractType contractType ;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "signer_id")
    private User signer;

    @ManyToOne()
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne()
    @JoinColumn(name = "job_grade_id")
    private JobGrade jobGrade;
}
