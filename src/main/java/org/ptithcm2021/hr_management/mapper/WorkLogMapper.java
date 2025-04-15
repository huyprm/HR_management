package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.ptithcm2021.hr_management.dto.response.WorkLogResponse;
import org.ptithcm2021.hr_management.model.WorkingHistory;

@Mapper(componentModel = "spring", uses = {
        DecisionMapper.class,
        ContractMapper.class,
})
public interface WorkLogMapper {
       WorkLogResponse toWorkLogResponse(WorkingHistory workLog);
    //    WorkLog toWorkLog(WorkLogRequest workLogRequest);
    //    void updateWorkLog(@MappingTarget WorkLog workLog, WorkLogUpdateRequest workLogUpdateRequest);
}
