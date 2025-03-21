package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.swing.text.rtf.RTFEditorKit;
import java.util.List;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {
    @Id
    private String id;

    private String fullName;
    private String numberCCCD;
    private String phoneNumber;
    private String email;
    private String dob;
    private String gender;
    private String address;
    private String ethnicity;
    private String religion;
    private String degree;
    private String status;
    private String avatar;

    @OneToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne()
    @JoinColumn(name = "degreeId")
    private Degree degreeId;

    @OneToMany(mappedBy = "user")
    private List<UserPosition> userPositions;

    @OneToMany(mappedBy = "user")
    private List<RewardAssignment> rewardAssignments;

    @OneToMany(mappedBy = "user")
    private List<DisciplineAssignment> disciplineAssignments;
}
