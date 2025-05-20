package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.JobGradeRequest;
import org.ptithcm2021.hr_management.dto.request.UpdateNameAndDescriptionRequest;
import org.ptithcm2021.hr_management.dto.response.JobGradeResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


public interface JobGradeService {
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    JobGradeResponse createJobGrade(JobGradeRequest jobGradeRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    JobGradeResponse updateJobGrade(String id, UpdateNameAndDescriptionRequest jobGradeRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    void deleteJobGrade(String id);

    JobGradeResponse getJobGrade(String id);

    List<JobGradeResponse> getAllJobGrade();

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    JobGradeResponse getJobGradeByUserId(long userId);
}
