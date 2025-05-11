package org.ptithcm2021.hr_management.model;

import antlr.ANTLRParser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class User extends Base{

    @Column(unique = true)
    @Email(message = "Email invalid")
    private String email;

    private String fullName;
    @NotNull
    private String numberCCCD;
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    private LocalDate dob;

    private String nationality;
    private String gender;
    private String address;
    private String ethnicity;
    private String religion;
    private String taxCode;
    private String degree;

    @Column()
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatusEnum status = UserStatusEnum.PENDING;

    private String avatar;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionId")
    private Position position;

    @Temporal(TemporalType.DATE)
    private LocalDate hireDate;

    private int serviceDuration;

    private double salaryBasic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seniorityAllowanceRuleId")
    private SeniorityAllowanceRule seniorityAllowanceRule;

}
