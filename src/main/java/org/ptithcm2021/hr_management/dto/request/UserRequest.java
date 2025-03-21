package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.model.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserRequest {
    private String fullName;
    private String numberCCCD;
    private String phoneNumber;
    private String email;
    private String dob;
    private String gender;
    private String address;
    private String ethnicity ;
    private String religion;
    private String degree;
    private String status;
    private boolean partyMember;
    private String avatar;
}
