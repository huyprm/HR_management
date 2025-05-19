package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.FeedbackRequest;
import org.ptithcm2021.hr_management.dto.response.FeedbackResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface FeedbackService {

    @PreAuthorize("T(String).valueOf(#feedbackRequest.userId) == authentication.name")
    FeedbackResponse createFeedback(FeedbackRequest feedbackRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    FeedbackResponse getFeedback(long id);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER')")
    List<FeedbackResponse> getAllFeedback();

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    List<FeedbackResponse> getAllFeedbackByUserId(long userId);
}
