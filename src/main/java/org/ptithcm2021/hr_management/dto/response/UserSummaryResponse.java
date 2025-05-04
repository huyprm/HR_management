package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryResponse {
    private long id;
    private String fullName;
    private String numberCCCD;
    private LocalDate dob;
    private String phoneNumber;
    private String nationality;
    private String email;
    private String address;
}
