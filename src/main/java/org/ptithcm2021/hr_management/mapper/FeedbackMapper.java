package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ptithcm2021.hr_management.dto.request.FeedbackRequest;
import org.ptithcm2021.hr_management.dto.response.FeedbackResponse;
import org.ptithcm2021.hr_management.model.Feedback;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    Feedback toFeedback(FeedbackRequest feedBackRequest);


    @Mapping(target = "sender.id", source = "sender.id")
    @Mapping(target = "sender.fullName", source = "sender.fullName")
    FeedbackResponse toFeedBackResponse(Feedback feedback);

}
