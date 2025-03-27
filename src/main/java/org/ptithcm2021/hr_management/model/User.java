package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;

import java.util.Date;
import java.util.List;

@Entity(name = "users")
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
    private Date dob;
    private String gender;
    private String address;
    private String ethnicity;
    private String religion;
    private String taxCode;
    private String degree;

    @Column
    private UserStatusEnum status = UserStatusEnum.PENDING;
    private String avatar;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "accountId")
    private Account account;

    @OneToMany(mappedBy = "user")
    private List<RewardAssignment> rewardAssignments;

    @OneToMany(mappedBy = "user")
    private List<DisciplineAssignment> disciplineAssignments;

    @OneToMany(mappedBy = "user")
    private List<Salary> salaries;

    @OneToMany(mappedBy = "user")
    private List<Contract> contracts;
}
