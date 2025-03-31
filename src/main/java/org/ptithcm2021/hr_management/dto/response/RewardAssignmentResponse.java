package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardAssignmentResponse {
    private String id;
    private String type;
    private String content;
    private Date date;
    private UserSummaryResponse user;
}
