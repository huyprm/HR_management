/**
 * Quy trình xử lý hợp đồng trong hệ thống HR Management:
 * 
 * 1. PENDING: Hợp đồng mới được tạo, đang chờ phê duyệt hoặc chờ ký kết
 * 2. SIGNED_PENDING_EFFECTIVE: Hợp đồng đã được ký, nhưng chưa có hiệu lực, sẽ có hiệu lực từ tháng sau
 * 3. ACTIVE: Hợp đồng đang có hiệu lực, là cơ sở để tính lương và phúc lợi
 * 4. EXPIRED: Hợp đồng đã hết hạn theo thời gian quy định
 * 5. TERMINATED: Hợp đồng bị chấm dứt trước thời hạn
 * 6. RENEWED: Hợp đồng đã được gia hạn bằng một hợp đồng mới
 */
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

    /**
     * Tạo hợp đồng mới với trạng thái PENDING (chờ phê duyệt/ký kết)
     */
    @Override
    public ContractResponse createDraftContract(ContractRequest request, boolean isExtend) {
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
        
        // Thiết lập trạng thái PENDING cho hợp đồng đang chờ phê duyệt/ký kết
        contract.setContractStatusEnum(ContractStatusEnum.PENDING);

        // Save contract
        Contract savedContract = contractRepository.save(contract);

        return contractMapper.toContractResponse(savedContract);
    }

    /**
     * Ký hợp đồng - chuyển từ PENDING sang SIGNED_PENDING_EFFECTIVE
     */
    @Override
    public ContractResponse signContract(int contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
        
        // Chỉ cho phép ký hợp đồng ở trạng thái PENDING
        if (contract.getContractStatusEnum() != ContractStatusEnum.PENDING) {
            throw new AppException(ErrorCode.CONTRACT_INVALID_STATUS);
        }
        
        // Cập nhật trạng thái hợp đồng thành đã ký, chờ hiệu lực
        contract.setContractStatusEnum(ContractStatusEnum.SIGNED_PENDING_EFFECTIVE);
        
        // Cập nhật thông tin người dùng
        User user = contract.getUser();
        Position position = contract.getPosition();
        
        // Update role for user's account
        user.getAccount().setRole(position.getRole().getId());
        user.setPosition(position);
        
        // Set hire date for new employees
        if (user.getHireDate() == null) {
            user.setHireDate(contract.getStartDate());
        }
        
        // Log event
        workLogRepository.save(
                WorkingHistory.builder()
                        .type(WorkLogTypeEnum.CONTRACT_SIGN)
                        .user(user)
                        .contract(contract)
                        .build()
        );
        
        // Create or update leave balance for policy-based contracts
        if (contract.getContractType().isPolicy()) {
            createOrUpdateLeaveBalance(user.getId(), contract.getStartDate(), contract.getEndDate());
        }
        
        // Save updated user and contract
        userRepository.save(user);
        Contract savedContract = contractRepository.save(contract);
        
        return contractMapper.toContractResponse(savedContract);
    }

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
        
        boolean isFirstContract = isFirstContract(user.getId());
        
        // Nếu là hợp đồng đầu tiên hoặc gia hạn hợp đồng -> có hiệu lực ngay
        // Nếu là hợp đồng thay đổi chức vụ -> chỉ có hiệu lực từ đầu tháng sau
        if (isFirstContract || isExtend) {
            contract.setContractStatusEnum(ContractStatusEnum.ACTIVE);
        } else {
            contract.setContractStatusEnum(ContractStatusEnum.SIGNED_PENDING_EFFECTIVE);
        }

        // Update role for user's account
        user.getAccount().setRole(position.getRole().getId());
        user.setPosition(position);
        
        // Create or update leave balance for policy-based contracts
        if (contractType.isPolicy()) {
            createOrUpdateLeaveBalance(user.getId(), contract.getStartDate(), contract.getEndDate());
        }

        // Save updated user and contract
        userRepository.save(user);
        Contract savedContract = contractRepository.save(contract);

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
        // Đầu tiên tìm hợp đồng ACTIVE
        Optional<Contract> activeContract = contractRepository.findContractByUserIdAndContractStatusEnum(
                userId, ContractStatusEnum.ACTIVE);
        
        if (activeContract.isPresent()) {
            return activeContract.get();
        }
        
        // Nếu không có hợp đồng ACTIVE, tìm hợp đồng đã ký chờ hiệu lực
        Optional<Contract> signedPendingContract = contractRepository.findContractByUserIdAndContractStatusEnum(
                userId, ContractStatusEnum.SIGNED_PENDING_EFFECTIVE);
        
        if (signedPendingContract.isPresent()) {
            return signedPendingContract.get();
        }
        
        // Cuối cùng tìm hợp đồng PENDING (trạng thái cũ)
        return contractRepository.findContractByUserIdAndContractStatusEnum(
                userId, ContractStatusEnum.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
    }

    @Override
    public List<Contract> getAllContractIsPending() {
        // Lấy cả hợp đồng đang chờ duyệt và hợp đồng đã ký chờ hiệu lực
        List<Contract> pendingContracts = contractRepository.findContractByContractStatusEnum(ContractStatusEnum.PENDING);
        List<Contract> signedPendingContracts = contractRepository.findContractByContractStatusEnum(
                ContractStatusEnum.SIGNED_PENDING_EFFECTIVE);
        
        // Kết hợp cả hai danh sách
        pendingContracts.addAll(signedPendingContracts);
        return pendingContracts;
    }

    @Override
    public ContractResponse updateContractWithPromotion(int contractId, String newJobGradeId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        JobGrade newJobGrade = jobGradeRepository.findById(newJobGradeId)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_GRADE_NOT_FOUND));

        JobGrade oldJobGrade = contract.getJobGrade();
        double oldSalary = contract.getBasicSalary();

        contract.setJobGrade(newJobGrade);
        
        // Lưu hợp đồng đã cập nhật
        Contract updatedContract = contractRepository.save(contract);
        
        return contractMapper.toContractResponse(updatedContract);
    }

    /**
     * Kiểm tra xem đây có phải là hợp đồng đầu tiên của người dùng không
     * @param userId ID của người dùng
     * @return true nếu là hợp đồng đầu tiên, false nếu người dùng đã có hợp đồng trước đó
     */
    private boolean isFirstContract(long userId) {
        List<Contract> userContracts = contractRepository.findContractByUserId(userId);
        return userContracts.isEmpty();
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
        // Kiểm tra xem người dùng đã có hợp đồng nào đang hoạt động hoặc chờ kích hoạt không
        List<Contract> existingContracts = contractRepository.findContractByUserId(userId);
        
        for (Contract contract : existingContracts) {
            ContractStatusEnum status = contract.getContractStatusEnum();
            
            // Kiểm tra nếu hợp đồng đang ACTIVE hoặc SIGNED_PENDING_EFFECTIVE
            if (status == ContractStatusEnum.ACTIVE || 
                status == ContractStatusEnum.SIGNED_PENDING_EFFECTIVE || 
                status == ContractStatusEnum.PENDING) {
                
                boolean overlaps = startDate.before(contract.getEndDate()) && 
                                  endDate.after(contract.getStartDate());
                
                if (overlaps) {
                    throw new AppException(ErrorCode.CONTRACT_OVERLAP);
                }
            }
        }
        
        // Kiểm tra ngày bắt đầu phải trước ngày kết thúc
        if (startDate.after(endDate)) {
            throw new AppException(ErrorCode.CONTRACT_OVERLAP);
        }
    }

}
