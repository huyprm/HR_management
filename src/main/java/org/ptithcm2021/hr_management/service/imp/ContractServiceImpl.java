package org.ptithcm2021.hr_management.service.imp;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.request.LeaveBalanceRequest;
import org.ptithcm2021.hr_management.dto.request.NotificationRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.ContractMapper;
import org.ptithcm2021.hr_management.model.*;
import org.ptithcm2021.hr_management.repository.*;
import org.ptithcm2021.hr_management.service.ContractService;
import org.ptithcm2021.hr_management.service.LeaveBalanceService;
import org.ptithcm2021.hr_management.service.NotificationService;
import org.ptithcm2021.hr_management.service.UserService;
import org.ptithcm2021.hr_management.util.LeaveBalanceUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final UserService userService;
    private final PositionRepository positionRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final JobGradeRepository jobGradeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final LeaveBalanceService leaveBalanceService;

    @Override
    public ContractResponse createContract(ContractRequest contractRequest, boolean isExtend) {
        User user = userService.getUserToUser(contractRequest.getUserId());
        User signer = userService.getUserToUser(contractRequest.getSignerId());

        Position position = positionRepository.findById(contractRequest.getPositionId())
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

        ContractType contractType = contractTypeRepository.findById(contractRequest.getContractTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_TYPE_NOT_FOUND));

        JobGrade jobGrade = jobGradeRepository.findById(contractRequest.getJobGradeId())
                .orElseThrow(() -> new AppException(ErrorCode.JOB_GRADE_NOT_FOUND));

        validateContractDates(contractRequest.getStartDate(), contractRequest.getEndDate());

        Contract contract = contractMapper.toContract(contractRequest);

        contract.setContractType(contractType);
        contract.setUser(user);
        contract.setSigner(signer);
        contract.setPosition(position);
        contract.setJobGrade(jobGrade);

        if (!isExtend) {
            user.getSeniorityAllowance().setHireDate(contract.getStartDate());
            userRepository.save(user);
        }

        if(contract.getContractType().isPolicy())
            createOrUpdateLeaveBalance(user.getId(), contract.getStartDate(), contract.getEndDate());

        return contractMapper.toContractResponse(contractRepository.save(contract));
    }

    @Override
    public ContractResponse getContract(int contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        return contractMapper.toContractResponse(contract);
    }

    @Override
    public List<ContractResponse> getAllContractByUser(long userId) {
        return contractRepository.findContractByUserId(userId)
                .stream().map(contractMapper::toContractResponse).toList();
    }

    @Override
    public List<ContractResponse> getAllContract(ContractStatusEnum contractStatusEnum) {
        if (contractStatusEnum == null){
            return contractRepository.findAll()
                    .stream().map(contractMapper::toContractResponse).toList();
        }
        return contractRepository.findContractByContractStatusEnum(contractStatusEnum)
                .stream().map(contractMapper::toContractResponse).toList();
    }

    @Override
    public void deleteContract(int contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        contract.setContractStatusEnum(ContractStatusEnum.TERMINATED);
    }

    @Override
    public ContractResponse extendContract(int contractId, ContractRequest contractRequest) {
        Contract oldContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        if (oldContract.getContractStatusEnum().equals(ContractStatusEnum.PENDING))
            throw  new AppException(ErrorCode.EXTEND_CONTRACT);

        oldContract.setContractStatusEnum(ContractStatusEnum.RENEWED);
        contractRepository.save(oldContract);

        return createContract(contractRequest, true);
    }

    @Override
    public ContractResponse getContractIsPendingByUserId(long userId) {
        Contract contract = contractRepository.findContractByUserIdAndContractStatusEnum(userId, ContractStatusEnum.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        return contractMapper.toContractResponse(contract);

    }

    private boolean validateContractDates(Date start, Date end) {
        return end == null || !end.before(start);
    }

    @Override
    public Contract getContractCurrentOfUser(long userId) {

        return contractRepository.findContractByUserIdAndContractStatusEnum(userId, ContractStatusEnum.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
    }

    @Override
    public List<Contract> getAllContractIsPendingOrExpirySoon() {
        return contractRepository.findByContractStatusEnumIn(List.of(ContractStatusEnum.PENDING, ContractStatusEnum.EXPIRING_SOON))
                .stream().toList();
    }

    private void createOrUpdateLeaveBalance(long userId, Date startDate, Date endDate){

        int calculatedDays = LeaveBalanceUtil.calculateLeaveDaysInYear(startDate, endDate);

        try {
            LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalanceToLeaveBalance(userId);

            int totalLeaveDay = Math.min(12, leaveBalance.getTotalLeaveDay() + calculatedDays);
            leaveBalance.setTotalLeaveDay(totalLeaveDay);

            leaveBalanceService.updateLeaveBalance(leaveBalance);
        } catch (AppException e) {
            LeaveBalanceRequest leaveBalanceRequest = new LeaveBalanceRequest();
            leaveBalanceRequest.setUserId(userId);
            leaveBalanceRequest.setUsedLeaveDay(0);
            leaveBalanceRequest.setCarriedOverDay(0);
            leaveBalanceRequest.setTotalLeaveDay(calculatedDays);

            leaveBalanceService.createLeaveBalance(leaveBalanceRequest);
        }
    }



}
