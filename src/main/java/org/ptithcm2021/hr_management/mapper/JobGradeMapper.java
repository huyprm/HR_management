package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.hr_management.dto.request.JobGradeRequest;
import org.ptithcm2021.hr_management.dto.response.JobGradeResponse;
import org.ptithcm2021.hr_management.model.JobGrade;

@Mapper(componentModel = "spring")
public interface JobGradeMapper {
    JobGrade toJobGrade(JobGradeRequest jobGradeRequest);

    JobGradeResponse toJobGradeResponse(JobGrade jobGrade);

    void updateJobGrade(@MappingTarget JobGrade jobGrade, JobGradeRequest jobGradeRequest);
}
