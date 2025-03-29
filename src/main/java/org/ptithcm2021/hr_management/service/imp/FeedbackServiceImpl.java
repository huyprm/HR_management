package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.FeedbackRequest;
import org.ptithcm2021.hr_management.dto.response.FeedbackResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.FeedbackMapper;
import org.ptithcm2021.hr_management.model.Feedback;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.FeedbackRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.ptithcm2021.hr_management.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public FeedbackResponse createFeedback(FeedbackRequest feedbackRequest) {
        User user = userRepository.findById(feedbackRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);

        feedback.setSender(user);

        return feedbackMapper.toFeedBackResponse(feedbackRepository.save(feedback));
    }

    @Override
    public FeedbackResponse getFeedback(long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));

        return feedbackMapper.toFeedBackResponse(feedback);
    }

    @Override
    public List<FeedbackResponse> getAllFeedback() {
        return feedbackRepository.findAll().stream().map(feedbackMapper::toFeedBackResponse).toList();
    }
}
