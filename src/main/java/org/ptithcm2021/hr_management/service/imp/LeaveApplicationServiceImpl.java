package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LeaveApplicationRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveApplicationResponse;
import org.ptithcm2021.hr_management.dto.response.LeaveTypeResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.LeaveApplicationMapper;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.LeaveApplicationRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.ptithcm2021.hr_management.service.LeaveApplicationService;
import org.ptithcm2021.hr_management.service.LeaveBalanceService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveApplicationServiceImpl implements LeaveApplicationService {
    private final LeaveApplicationMapper leaveApplicationMapper;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LeaveBalanceService leaveBalanceService;

    @Override
    public LeaveApplicationResponse createApplication(LeaveApplicationRequest leaveApplicationRequest) {
        LeaveApplication leaveApplication = leaveApplicationMapper.toLeaveApplication(leaveApplicationRequest);

        User user = userService.getUserToUser(leaveApplicationRequest.getUserId());
        leaveApplication.setUser(user);

        return leaveApplicationMapper.toLeaveTypeApplicationResponse(
                leaveApplicationRepository.save(leaveApplication)
        );
    }

    @Override
    public LeaveApplicationResponse confirmApplication(FormStatusEnum formStatusEnum, long applicationId) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_APPLICATION_NOT_FOUND));

        leaveApplication.setFormStatusEnum(formStatusEnum);

        if (FormStatusEnum.APPROVED.equals(formStatusEnum))
            leaveBalanceService.dayOff(Year.now().getValue(), leaveApplication);

        return leaveApplicationMapper.toLeaveTypeApplicationResponse(
                leaveApplicationRepository.save(leaveApplication)
        );
    }

    @Override
    public List<LeaveApplicationResponse> getApplicationIsPending(FormStatusEnum formStatusEnum) {
        if (formStatusEnum == null)
            return leaveApplicationRepository.findAll()
                .stream().map(leaveApplicationMapper::toLeaveTypeApplicationResponse).toList();

        return leaveApplicationRepository.getAllByFormStatusEnum(formStatusEnum)
                .stream().map(leaveApplicationMapper::toLeaveTypeApplicationResponse).toList();
    }

    @Override
    public LeaveApplicationResponse getApplication(long applicationId) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_APPLICATION_NOT_FOUND));

        return leaveApplicationMapper.toLeaveTypeApplicationResponse(
                leaveApplicationRepository.save(leaveApplication)
        );
    }
}
