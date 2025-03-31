package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.validator.StringNumberConstraint;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String fullName;

    @StringNumberConstraint(type = StringNumberConstraint.NumberType.CCCD)
    private String numberCCCD;

    @StringNumberConstraint(type = StringNumberConstraint.NumberType.PHONE)
    private String phoneNumber;

    @Email(message = "Email cannot be empty")
    private String email;

    private Date dob;
    private String gender;
    private String address;
    private String ethnicity ;
    private String religion;
    private String avatar;
    private String degree;
    private String taxCode;

    @NotNull(message = "Role cannot be empty")
    private RoleEnum roleId;
}
