package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LeaveTypeRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveTypeResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.LeaveTypeMapper;
import org.ptithcm2021.hr_management.model.LeaveType;
import org.ptithcm2021.hr_management.repository.LeaveTypeRepository;
import org.ptithcm2021.hr_management.service.LeaveTypeService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveTypeMapper leaveTypeMapper;

    @Override
    public LeaveTypeResponse createLeaveType(LeaveTypeRequest leaveTypeRequest) {
        LeaveType leaveType = leaveTypeMapper.toLeaveType(leaveTypeRequest);
        return leaveTypeMapper.toLeaveTypeResponse(leaveTypeRepository.save(leaveType));
    }

    @Override
    public LeaveTypeResponse updateLeaveType(LeaveTypeRequest leaveTypeRequest, int id) {
        LeaveType leaveType = leaveTypeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_TYPE_NOT_FOUND));

        leaveTypeMapper.updateLeaveType(leaveType, leaveTypeRequest);

        return leaveTypeMapper.toLeaveTypeResponse(leaveTypeRepository.save(leaveType));
    }

    @Override
    public LeaveTypeResponse getLeaveType(int id) {
        LeaveType leaveType = leaveTypeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_TYPE_NOT_FOUND));

        return leaveTypeMapper.toLeaveTypeResponse(leaveTypeRepository.save(leaveType));
    }

    @Override
    public List<LeaveTypeResponse> getAllLeaveType() {
        return leaveTypeRepository.findAll()
                .stream().map(leaveTypeMapper::toLeaveTypeResponse).toList();
    }

    @Override
    public void deleteLeaveType(int id) {
        if (!leaveTypeRepository.existsById(id))
            throw new AppException(ErrorCode.LEAVE_TYPE_NOT_FOUND);

        try{
            leaveTypeRepository.deleteById(id);
        }catch (Exception e){
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }
}
