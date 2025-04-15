package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.request.LeaveBalanceRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.enums.WorkLogTypeEnum;
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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private final LeaveBalanceService leaveBalanceService;
    private final WorkLogRepository workLogRepository;

    @Override
    public ContractResponse createContract(ContractRequest request, boolean isExtend) {
        validateNoActiveContract(request.getUserId(), request.getStartDate(), request.getEndDate());

        User user = userService.getUserToUser(request.getUserId());
        User signer = userService.getUserToUser(request.getSignerId());

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

        ContractType contractType = contractTypeRepository.findById(request.getContractTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_TYPE_NOT_FOUND));

        JobGrade jobGrade = jobGradeRepository.findById(request.getJobGradeId())
                .orElseThrow(() -> new AppException(ErrorCode.JOB_GRADE_NOT_FOUND));

        Contract contract = contractMapper.toContract(request);
        contract.setUser(user);
        contract.setSigner(signer);
        contract.setPosition(position);
        contract.setContractType(contractType);
        contract.setJobGrade(jobGrade);

        // Update role for user's account
        user.getAccount().setRole(position.getRole().getId());
        user.setPosition(position);

        // Set hire date only for new contracts (not extensions)
        if (!isExtend) {
            user.setHireDate(contract.getStartDate());
            workLogRepository.save(
                    WorkingHistory.builder()
                            .type(WorkLogTypeEnum.CONTRACT_SIGN)
                            .user(user)
                            .contract(contract)
                            .build()
            );
        } else {
            workLogRepository.save(
                    WorkingHistory.builder()
                            .type(WorkLogTypeEnum.CONTRACT_RENEWAL)
                            .user(user)
                            .contract(contract)
                            .build()
            );
        }
        // Create or update leave balance for policy-based contracts
        if (contractType.isPolicy()) {
            createOrUpdateLeaveBalance(user.getId(), contract.getStartDate(), contract.getEndDate());
        }

        // Save updated user and contract
        userRepository.save(user);
        Contract savedContract = contractRepository.save(contract);

        return contractMapper.toContractResponse(savedContract);
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

        contract.getUser().setStatus(UserStatusEnum.TERMINATED);
        contract.getUser().getAccount().setStatus(false);
        contractRepository.save(contract);
    }

    @Override
    public ContractResponse extendContract(int contractId, ContractRequest request) {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        Date currentDate = new Date();
        long daysBeforeEnd = (existingContract.getEndDate().getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24);

        // Allow renewal only if the contract is within 30 days of expiration or already expired
        if (daysBeforeEnd > 30) {
            throw new AppException(ErrorCode.CONTRACT_NOT_ELIGIBLE_FOR_RENEWAL);
        }

        if (existingContract.getContractStatusEnum() == ContractStatusEnum.TERMINATED) {
            throw new AppException(ErrorCode.EXTEND_CONTRACT);
        }

        existingContract.setContractStatusEnum(ContractStatusEnum.RENEWED);
        contractRepository.save(existingContract);

        // Create a new contract as an extension
        return createContract(request, true);
    }

    @Override
    public ContractResponse getContractIsPendingByUserId(long userId) {
        Contract contract = contractRepository.findContractByUserIdAndContractStatusEnum(userId, ContractStatusEnum.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        return contractMapper.toContractResponse(contract);

    }

    @Override
    public Contract getContractCurrentOfUser(long userId) {

        return contractRepository.findContractByUserIdAndContractStatusEnum(userId, ContractStatusEnum.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
    }

    @Override
    public List<Contract> getAllContractIsPending() {
        return contractRepository.findContractByContractStatusEnum(ContractStatusEnum.PENDING)
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

    private void validateNoActiveContract(long userId, Date startDate, Date endDate) {
        Optional<Contract> optionalContract = contractRepository
                .findContractByUserIdAndContractStatusEnum(userId, ContractStatusEnum.PENDING);

        if (optionalContract.isEmpty()) {
            if (startDate.after(endDate)) throw new AppException(ErrorCode.CONTRACT_OVERLAP);
            return;
        }

        Contract contract = optionalContract.get();
        boolean overlaps =
                startDate.before(contract.getEndDate()) && endDate.after(contract.getStartDate());

        if (overlaps) {
            throw new AppException(ErrorCode.CONTRACT_OVERLAP);
        }
    }



}
