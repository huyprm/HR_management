package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.hr_management.enums.PaymentStatusEnum;

import java.time.YearMonth;
import java.util.Date;

@Entity(name = "salaries")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table()
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private YearMonth salaryMonth;
    private double totalAllowance;
    private double unpaidLeaveDeduction;
    private double baseSalary;

    private  Date paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId")
    private Contract contract;

}
