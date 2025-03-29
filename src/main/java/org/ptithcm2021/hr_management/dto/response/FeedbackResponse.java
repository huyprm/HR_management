package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private long id;
    private String title;
    private String content;
    private LocalDateTime sendDate;

    private UserSummaryResponse sender;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummaryResponse {
        private long id;
        private String fullName;
    }
}
