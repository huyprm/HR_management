package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LeaveDayRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveDayResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.LeaveDayMapper;
import org.ptithcm2021.hr_management.model.LeaveDay;
import org.ptithcm2021.hr_management.repository.LeaveDayRepository;
import org.ptithcm2021.hr_management.service.LeaveDayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveDayServiceImpl implements LeaveDayService {
    private final LeaveDayMapper leaveDayMapper;
    private final LeaveDayRepository leaveDayRepository;

    @Override
    public LeaveDayResponse createLeaveDay(LeaveDayRequest leaveDayRequest) {
        LeaveDay leaveDay = leaveDayMapper.toLeaveDay(leaveDayRequest);

        return leaveDayMapper.toLeaveDayResponse(leaveDayRepository.save(leaveDay));
    }

    @Override
    public LeaveDayResponse updateLeaveDay(LeaveDayRequest leaveDayRequest, int leaveDayId) {
        LeaveDay leaveDay = leaveDayRepository.findById(leaveDayId)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_DAY_NOT_FOUND));

        leaveDayMapper.updateLeaveDay(leaveDay, leaveDayRequest);

        return leaveDayMapper.toLeaveDayResponse(leaveDayRepository.save(leaveDay));
    }

    @Override
    public void deleteLeaveDay(int leaveDayId) {
        if (!leaveDayRepository.existsById(leaveDayId))
            throw new AppException(ErrorCode.LEAVE_DAY_NOT_FOUND);

        leaveDayRepository.deleteById(leaveDayId);
    }

    @Override
    public LeaveDayResponse getLeaveDay(int leaveDayId) {
        LeaveDay leaveDay = leaveDayRepository.findById(leaveDayId)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVE_DAY_NOT_FOUND));

        return leaveDayMapper.toLeaveDayResponse(leaveDay);
    }

    @Override
    public List<LeaveDayResponse> getListLeaveDayByMonth(YearMonth yearMonth) {

        return leaveDayRepository.findAllByMonth(yearMonth.atDay(1), yearMonth.atEndOfMonth())
                .stream().map(leaveDayMapper::toLeaveDayResponse).toList();
    }
}
