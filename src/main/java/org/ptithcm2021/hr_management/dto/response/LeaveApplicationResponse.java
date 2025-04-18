package org.ptithcm2021.hr_management.dto.response;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplicationResponse {
    private long id;
    private Date startDate;
    private Date endDate;

    private String reason;

    private FormStatusEnum formStatusEnum;

    private UserSummaryResponse user;
    private UserSummaryResponse signer;

    private String leaveTypeName;
}
