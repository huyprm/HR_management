package org.ptithcm2021.hr_management.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.hr_management.model.User;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardDecisionResponse {
    private String id;
    private String type;
    private String content;
    private Date date;
    private FeedbackResponse.UserSummaryResponse signer;
}
