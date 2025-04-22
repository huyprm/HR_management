package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private long id;
    private String email;
    private String fullName;
    private String numberCCCD;
    private String phoneNumber;
    private String nationality;
    private Date dob;
    private String gender;
    private String address;
    private String ethnicity;
    private String religion;
    private String taxCode;
    private String degree;
    private UserStatusEnum status;
    private String avatar;
    private String departmentName;
    private String positionName;
    private int numReward;
    private int numDiscipline;
    private Date hireDate;
    private int serviceDuration;
    private double seniorityPercentage;
    private int seniorityLeaveDay;
    private int carriedOverDay;
    private int usedLeaveDay;
    private Page<NotificationRecipientResponse> recipientResponse;
}
