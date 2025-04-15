package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;

import java.util.Date;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User extends Base{

    @Column(unique = true)
    @Email(message = "Email invalid")
    private String email;

    private String fullName;
    @NotNull
    private String numberCCCD;
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    private Date dob;

    private String gender;
    private String address;
    private String ethnicity;
    private String religion;
    private String taxCode;
    private String degree;

    @Column()
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status = UserStatusEnum.PENDING;

    private String avatar;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionId")
    private Position position;

    @Temporal(TemporalType.DATE)
    private Date hireDate;

    private int serviceDuration;

    private double salaryBasic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seniorityAllowanceRuleId")
    private SeniorityAllowanceRule seniorityAllowanceRule;
}
