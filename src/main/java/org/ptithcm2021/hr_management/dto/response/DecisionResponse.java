package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionResponse {
    private String id;
    private String type;
    private String content;
    private Date date;
    private FeedbackResponse.UserSummaryResponse signer;
}
