package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String email;
    private String fullName;
    private String numberCCCD;
    private String phoneNumber;
    private Date dob;
    private String gender;
    private String address;
    private String ethnicity;
    private String religion;
    private String taxCode;
    private String degree;
    private UserStatusEnum status;
    private String avatar;

}
