package org.ptithcm2021.hr_management.service.impl;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LeaveApplicationRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveApplicationResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.LeaveApplicationMapper;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.ptithcm2021.hr_management.model.LeaveType;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.LeaveApplicationRepository;
import org.ptithcm2021.hr_management.repository.LeaveBalanceRepository;
import org.ptithcm2021.hr_management.repository.LeaveTypeRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.ptithcm2021.hr_management.service.LeaveApplicationService;
import org.ptithcm2021.hr_management.service.LeaveBalanceService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveApplicationServiceImpl implements LeaveApplicationService {
    private final LeaveApplicationMapper leaveApplicationMapper;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LeaveBalanceService leaveBalanceService;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    @Override
    public LeaveApplicationResponse createApplication(LeaveApplicationRequest leaveApplicationRequest) {
        LeaveApplication leaveApplication = leaveApplicationMapper.toLeaveApplication(leaveApplicationRequest);

        User user = userService.getUserToUser(leaveApplicationRequest.getUserId());
        LeaveType leaveType = leaveTypeRepository.findById(leaveApplicationRequest.getLeaveTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_TYPE_NOT_FOUND));

        leaveApplication.setUser(user);
        leaveApplication.setLeaveType(leaveType);


        return leaveApplicationMapper.toLeaveTypeApplicationResponse(
                leaveApplicationRepository.save(leaveApplication)
        );
    }

    @Override
    public LeaveApplicationResponse confirmApplication(FormStatusEnum formStatusEnum, long applicationId) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userIdStr == null) throw new AppException(ErrorCode.UNAUTHORIZED);

        long userId = Long.parseLong(userIdStr);
        User signer = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        LeaveApplication leaveApplication = leaveApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_APPLICATION_NOT_FOUND));

        if(leaveApplication.getFormStatusEnum() != FormStatusEnum.PENDING)
            throw new AppException(ErrorCode.FORM_STATUS_INVALID);

        leaveApplication.setFormStatusEnum(formStatusEnum);
        leaveApplication.setSigner(signer);

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

    @Override
    public List<LeaveApplicationResponse> getApplicationByUserId(long userId) {
        return leaveApplicationRepository.findAllByUserId(userId)
                .stream().map(leaveApplicationMapper::toLeaveTypeApplicationResponse).toList();
    }
}
