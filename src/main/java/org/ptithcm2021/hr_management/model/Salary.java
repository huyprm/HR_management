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
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private YearMonth salaryMonth;
    private double totalAllowance;
    private double unpaidLeaveDeduction;
    private  Date paymentDate;
    private PaymentStatusEnum paymentStatus;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "contractId")
    private Contract contract;

}
