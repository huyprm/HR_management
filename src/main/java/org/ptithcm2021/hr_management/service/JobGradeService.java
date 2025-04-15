package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.JobGradeRequest;
import org.ptithcm2021.hr_management.dto.request.UpdateNameAndDescriptionRequest;
import org.ptithcm2021.hr_management.dto.response.JobGradeResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
public interface JobGradeService {
    JobGradeResponse createJobGrade(JobGradeRequest jobGradeRequest);

    JobGradeResponse updateJobGrade(String id, UpdateNameAndDescriptionRequest jobGradeRequest);

    void deleteJobGrade(String id);

    JobGradeResponse getJobGrade(String id);

    List<JobGradeResponse> getAllJobGrade();
}
