package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.LeaveTypeRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveTypeResponse;
import org.ptithcm2021.hr_management.model.LeaveType;

import java.util.List;

public interface LeaveTypeService {
    LeaveTypeResponse createLeaveType(LeaveTypeRequest leaveTypeRequest);
    LeaveTypeResponse updateLeaveType(LeaveTypeRequest leaveTypeRequest, int id);
    LeaveTypeResponse getLeaveType(int id);
    List<LeaveTypeResponse> getAllLeaveType();
    void deleteLeaveType (int id);
}
