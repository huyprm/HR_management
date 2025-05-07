package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private long id;
    private String title;
    private String content;
    private List<String> attached;
    private LocalDateTime sendDate;
    private String recipientText;
    private FeedbackResponse.UserSummaryResponse sender;
}
