package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;

import java.time.Instant;

@Entity(name = "contracts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contract{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Instant startDate;
    private Instant endDate;
    private double basicSalary;
    private boolean status;
    private String clause;
    private double allowance;
    private ContractStatusEnum contractStatusEnum;
    @ManyToOne()
    @JoinColumn(name = "contractTypeId")
    private ContractType contractType;

    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "positionId")
    private Position position;

    @ManyToOne()
    @JoinColumn(name = "jobGradeId")
    private JobGrade jobGrade;

}
