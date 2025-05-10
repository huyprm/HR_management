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
package org.ptithcm2021.hr_management.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.enums.WorkLogTypeEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.ContractMapper;
import org.ptithcm2021.hr_management.model.*;
import org.ptithcm2021.hr_management.repository.*;
import org.ptithcm2021.hr_management.schedule.ContractSchedule;
import org.ptithcm2021.hr_management.service.ContractService;
import org.ptithcm2021.hr_management.service.FileService;
import org.ptithcm2021.hr_management.service.LeaveBalanceService;
import org.ptithcm2021.hr_management.service.UserService;
import org.ptithcm2021.hr_management.util.FillDocxWithTagsUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


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
    private final WorkLogRepository workLogRepository;
    private final FileService fileService;
    private final ContractSchedule contractSchedule;

    /**
     * Tạo hợp đồng mới với trạng thái PENDING (chờ phê duyệt/ký kết)
     */
    @Override
    public ContractResponse createDraftContract(ContractRequest request) throws Exception {
        validateNoActiveContract(request.getUserId(), request.getStartDate(), request.getEndDate());

        User user = userService.getUserToUser(request.getUserId());
        User signer = userService.getUserToUser(request.getSignerId());

        if(signer.getId()==user.getId()){
            throw new AppException(ErrorCode.SIGNER_IS_USER);
        }

//        if (!signer.getPosition().getRole().getId().equals(RoleEnum.ADMIN)) {
//            throw new AppException(ErrorCode.RIGHT_SIGNER);
//        }

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

        // Tạo file hợp đồng
        String clause;
        try {
            Map<String, String> data = getContractTemplateData(savedContract);
            ByteArrayOutputStream byteArrayOutputStream = FillDocxWithTagsUtil
                    .fillDocxWithTags(data);

            clause = fileService.uploadFileFromByteArrayOutputStream(byteArrayOutputStream, "HD00" + contract.getId() );
            savedContract.setClause(clause);

            contractRepository.save(savedContract);
        }catch (Exception e){
            throw e;
        }
        return contractMapper.toContractResponse(savedContract);
    }

    /**
     * Ký hợp đồng - chuyển từ PENDING sang SIGNED_PENDING_EFFECTIVE
     */
    @Override
    public ContractResponse signContract(int contractId, String clause) throws Exception {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        // Chỉ cho phép ký hợp đồng ở trạng thái PENDING
        if (contract.getContractStatusEnum() != ContractStatusEnum.PENDING) {
            throw new AppException(ErrorCode.CONTRACT_INVALID_STATUS);
        }

        fileService.deleteFile(contract.getClause());

        // Cập nhật trạng thái hợp đồng thành đã ký, chờ hiệu lực và lập lịch
        contract.setClause(clause);

        if( contract.getStartDate().isBefore(LocalDate.now())){
            contract.setContractStatusEnum(ContractStatusEnum.ACTIVE);
            contract.getUser().setStatus(UserStatusEnum.ACTIVE);
        } else {
            contract.setContractStatusEnum(ContractStatusEnum.SIGNED_PENDING_EFFECTIVE);

            contractSchedule.scheduleContractStatusUpdate(
                    contractId,
                    contract.getStartDate().atStartOfDay(),
                    ContractStatusEnum.ACTIVE);
        }

        // Cập nhật thông tin người dùng
        User user = contract.getUser();
        Position position = contract.getPosition();
        double basicSalary = contract.getBasicSalary() * contract.getJobGrade().getCoefficient();

        // Update role for user's account
        user.getAccount().setRole(position.getRole().getId());
        user.setPosition(position);
        user.setSalaryBasic(basicSalary);

        // Set hire date for new employees
        if (user.getHireDate() == null) {
            user.setHireDate(contract.getStartDate());

            workLogRepository.save(
                    WorkingHistory.builder()
                            .type(WorkLogTypeEnum.CONTRACT_SIGN)
                            .user(user)
                            .contract(contract)
                            .build()
            );
        }else {
            workLogRepository.save(
                    WorkingHistory.builder()
                            .type(WorkLogTypeEnum.CONTRACT_RENEWAL)
                            .user(user)
                            .contract(contract)
                            .build()
            );
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
    public PagedModel<ContractResponse> getAllContractByUser(long userId, ContractStatusEnum contractStatusEnum, Pageable pageable) {
        return new PagedModel<>(contractRepository.findContractByUserIdAndContractStatusEnum(
                userId,
                contractStatusEnum,
                pageable)
                .map(contractMapper::toContractResponse));
    }

    @Override
    public PagedModel<ContractResponse> getAllContract(ContractStatusEnum contractStatusEnum, Pageable pageable) {
        Page<Contract> contractPage;

        if (contractStatusEnum == null) {
            contractPage = contractRepository.findAll(pageable);
        } else {
            contractPage = contractRepository.findAllContractByContractStatusEnum(contractStatusEnum, pageable);
        }
        return new PagedModel<>(contractPage.map(contractMapper::toContractResponse));
    }

    @Override
    public ContractResponse getContractIsActiveByUser(long userId) {
        Optional<Contract> activeContract = contractRepository.findContractByUserIdAndContractStatusEnum(
                userId, ContractStatusEnum.ACTIVE);

        if (activeContract.isPresent()) {
            return contractMapper.toContractResponse(activeContract.get());
        }

        Optional<Contract> signedContract = contractRepository.findContractByUserIdAndContractStatusEnum(
                userId, ContractStatusEnum.SIGNED_PENDING_EFFECTIVE);

        if (signedContract.isPresent()) {
            return contractMapper.toContractResponse(signedContract.get());
        }

        Optional<Contract> expiredContract = contractRepository.findContractByUserIdAndContractStatusEnum(
                userId, ContractStatusEnum.EXPIRED);

        return expiredContract.map(contractMapper::toContractResponse)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
    }

    @Override
    public void deleteContract(int contractId) throws Exception {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        if(contract.getContractStatusEnum() == ContractStatusEnum.PENDING) {
            fileService.deleteFile(contract.getClause());
            contractRepository.delete(contract);
            return;
        }
        contract.setContractStatusEnum(ContractStatusEnum.TERMINATED);

        contract.getUser().setStatus(UserStatusEnum.TERMINATED);
        contract.getUser().getAccount().setStatus(false);
        contractRepository.save(contract);
    }

    @Override
    public ContractResponse extendContract(int contractId, ContractRequest request) throws Exception {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

       LocalDate now = LocalDate.now();
       long daysBeforeEnd = ChronoUnit.DAYS.between(now, existingContract.getEndDate());

        if (existingContract.getContractStatusEnum() == ContractStatusEnum.TERMINATED) {
            throw new AppException(ErrorCode.EXTEND_CONTRACT);
        }

        contractSchedule.scheduleContractStatusUpdate(
                contractId,
                existingContract.getEndDate().plusDays(1).atStartOfDay().plusHours(1),
                ContractStatusEnum.RENEWED);

        // Create a new contract as an extension
        return createDraftContract(request);
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

        return signedPendingContract.orElse(null);
    }

    @Override
    public ContractResponse updateContractWithPromotion(int contractId, String newJobGradeId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        JobGrade newJobGrade = jobGradeRepository.findById(newJobGradeId)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_GRADE_NOT_FOUND));

        contract.setJobGrade(newJobGrade);
        contract.getUser().setSalaryBasic(contract.getBasicSalary() * newJobGrade.getCoefficient());

        Contract updatedContract = contractRepository.save(contract);

        return contractMapper.toContractResponse(updatedContract);
    }

    @Override
    public ContractResponse updateContract(int contractId, ContractRequest contractRequest) throws Exception {
        Contract  contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        if(!Objects.equals(contract.getPosition().getId(), contractRequest.getPositionId())){
            Position position = positionRepository.findById(contractRequest.getPositionId())
                    .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

            contract.setPosition(position);
        }

        if (contract.getSigner().getId() != contractRequest.getSignerId()){
            User signer = userService.getUserToUser(contractRequest.getSignerId());

            if (signer.getId() == contract.getUser().getId()){
                throw new AppException(ErrorCode.SIGNER_IS_USER);
            }

            if (!signer.getPosition().getRole().getId().equals(RoleEnum.ADMIN)) {
                throw new AppException(ErrorCode.RIGHT_SIGNER);
            }

            contract.setSigner(signer);
        }

        if (!Objects.equals(contract.getContractType().getId(), contractRequest.getContractTypeId())){
            ContractType contractType = contractTypeRepository.findById(contractRequest.getContractTypeId())
                    .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_TYPE_NOT_FOUND));

            contract.setContractType(contractType);
        }

        if (!contract.getJobGrade().getId().equals(contractRequest.getJobGradeId())){
            JobGrade jobGrade = jobGradeRepository.findById(contractRequest.getJobGradeId())
                    .orElseThrow(() -> new AppException(ErrorCode.JOB_GRADE_NOT_FOUND));

            contract.setJobGrade(jobGrade);
        }

        contract.setStartDate(contractRequest.getStartDate());
        contract.setEndDate(contractRequest.getEndDate());
        contract.setBasicSalary(contractRequest.getBasicSalary());

        // Xóa rồi tạo file mới
        fileService.deleteFile(contract.getClause());

        String clause;
        Map<String, String> data = getContractTemplateData(contract);
        ByteArrayOutputStream byteArrayOutputStream = FillDocxWithTagsUtil
                .fillDocxWithTags(data);

        clause = fileService.uploadFileFromByteArrayOutputStream(byteArrayOutputStream, "HD00" + contract.getId() );
        contract.setClause(clause);

        return contractMapper.toContractResponse(contractRepository.save(contract));
    }

    @Override
    public List<ContractResponse> getContractsByUserIdAndStatusNotActive(long userId) {
        return contractRepository.findAllContractByUserIdIsNotActive(userId)
                .stream().map(contractMapper::toContractResponse).collect(Collectors.toList());
    }

    private void validateNoActiveContract(long userId, LocalDate startDate, LocalDate endDate) {
        // Kiểm tra xem người dùng đã có hợp đồng nào đang hoạt động hoặc chờ kích hoạt không
        List<Contract> existingContracts = contractRepository.findContractByUserId(userId);

        for (Contract contract : existingContracts) {
            ContractStatusEnum status = contract.getContractStatusEnum();

            // Kiểm tra nếu hợp đồng đang ACTIVE hoặc SIGNED_PENDING_EFFECTIVE
            if (status == ContractStatusEnum.ACTIVE ||
                status == ContractStatusEnum.SIGNED_PENDING_EFFECTIVE ||
                status == ContractStatusEnum.PENDING) {

                boolean overlaps = startDate.isBefore(contract.getEndDate()) &&
                                  endDate.isAfter(contract.getStartDate());

                if (overlaps) {
                    throw new AppException(ErrorCode.CONTRACT_OVERLAP);
                }
            }
        }

        // Kiểm tra ngày bắt đầu phải trước ngày kết thúc
        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.CONTRACT_OVERLAP);
        }
    }

    private Map<String, String> getContractTemplateData(Contract contract) {

        Map<String, String> data = new HashMap<>();

        data.put("location", "TP.HCM");
        data.put("date", String.valueOf(LocalDate.now().getDayOfMonth()));
        data.put("month", String.valueOf(LocalDate.now().getMonthValue()));
        data.put("year", String.valueOf(LocalDate.now().getYear()));
        data.put("id", String.valueOf(contract.getId()));
        data.put("fullNameA", contract.getSigner().getFullName());
        data.put("positionA", contract.getSigner().getPosition().getName());
        data.put("addressA", contract.getSigner().getAddress());
        data.put("phoneA", contract.getSigner().getPhoneNumber());
        data.put("fullNameB", contract.getUser().getFullName());
        data.put("dobB", contract.getUser().getDob().toString());
        data.put("nationality", contract.getUser().getNationality());
        data.put("addressB", contract.getUser().getAddress());
        data.put("cccd", contract.getUser().getNumberCCCD());
        data.put("typeContract", contract.getContractType().getName());
        data.put("duration", contract.getContractType().getDuration());
        data.put("startDate", String.valueOf(contract.getStartDate().getDayOfMonth()));
        data.put("startMonth", String.valueOf(contract.getStartDate().getMonth()));
        data.put("startYear", String.valueOf(contract.getStartDate().getYear()));
        data.put("endDate", String.valueOf(contract.getEndDate().getDayOfMonth()));
        data.put("endMonth", String.valueOf(contract.getEndDate().getMonth()));
        data.put("endYear", String.valueOf(contract.getEndDate().getYear()));
        data.put("departmentName", contract.getPosition().getDepartment().getName());
        data.put("positionB", contract.getPosition().getName());
        data.put("salary", String.valueOf(contract.getBasicSalary() * contract.getJobGrade().getCoefficient()));

        return data;
    }
}
