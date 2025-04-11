package org.ptithcm2021.hr_management.service.imp;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.ChangePasswordRequest;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.request.UserUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.NotificationRecipientResponse;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.UserMapper;
import org.ptithcm2021.hr_management.model.*;
import org.ptithcm2021.hr_management.repository.*;
import org.ptithcm2021.hr_management.service.ContractService;
import org.ptithcm2021.hr_management.service.MailService;
import org.ptithcm2021.hr_management.service.SeniorityAllowanceService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final NotificationRecipientRepository notificationRecipientRepository;
    private final ContractRepository contractRepository;
    private final RewardAssignmentRepository rewardAssignmentRepository;
    private final DisciplineAssignmentRepository disciplineAssignmentRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;


    @Override
    public UserResponse createUser(UserRequest userRequest) throws MessagingException {
        User user = userMapper.toUser(userRequest);

        Random random = new Random();
        String password = String.format("%08d", random.nextInt(100_000_000));

        Account account = Account.builder()
                .username(user.getEmail())
                .password(passwordEncoder.encode(password))
                .build();

        user.setAccount(account);

        SeniorityAllowance seniorityAllowance = new SeniorityAllowance();
        seniorityAllowance.setUser(user);

        user.setSeniorityAllowance(seniorityAllowance);

        User result = userRepository.save(user);

        String message = createSendPWMessage(account.getUsername(), password, result.getFullName());

        mailService.sendMimeEmail(result.getEmail(), message, "Thông Tin Tài Khoản");
        return userMapper.toUserResponse(result);
    }

    @Override
    public UserResponse getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(user.getStatus().equals(UserStatusEnum.TERMINATED)) throw new AppException(ErrorCode.USER_TERMINATED);

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setRecipientResponse(getTop5NotificationRecipient(id));

        return userResponse;
    }

    @Override
    public UserResponse updateUser(long id, UserUpdateRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, userRequest);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setStatus(UserStatusEnum.TERMINATED);
        user.getAccount().setStatus(false);
        userRepository.save(user);
    }

    @Override
    public UserResponse fetchInfoUser() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userIdStr == null) throw new AppException(ErrorCode.UNAUTHORIZED);

        long userId = Long.parseLong(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setRecipientResponse(getTop5NotificationRecipient(userId));

        // Basic Info
        userResponse.setDepartmentName(
                Optional.ofNullable(user.getDepartment())
                        .map(Department::getName)
                        .orElse(null)
        );

        // Seniority Info
        SeniorityAllowance seniority = user.getSeniorityAllowance();
        userResponse.setServiceDuration(Optional.ofNullable(seniority).map(SeniorityAllowance::getServiceDuration).orElse(0));
        userResponse.setHireDate(Objects.requireNonNull(seniority).getHireDate());

        SeniorityAllowanceRule rule = Optional.of(seniority)
                .map(SeniorityAllowance::getSeniorityAllowanceRule)
                .orElse(null);

        userResponse.setSeniorityPercentage(rule != null ? rule.getSeniorityPercentage() : 0);
        userResponse.setSeniorityLeaveDay(rule != null ? rule.getSeniorityLeaveDay() : 0);

        // Position Info
        Contract contract = contractRepository.findContractByUserIdAndContractStatusEnum(userId, ContractStatusEnum.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
        userResponse.setPositionName(
                Optional.ofNullable(contract.getPosition())
                        .map(Position::getName)
                        .orElse(null)
        );

        // Rewards & Discipline
        userResponse.setNumReward(Math.toIntExact(rewardAssignmentRepository.countByUserId(userId)));
        userResponse.setNumDiscipline(Math.toIntExact(disciplineAssignmentRepository.countByUserId(userId)));

        // Leave Balance
        LeaveBalance leave = leaveBalanceRepository.findByUserIdAndYear(userId, Year.now().getValue()).orElse(null);
        userResponse.setUsedLeaveDay(leave != null ? leave.getUsedLeaveDay() : 0);
        userResponse.setCarriedOverDay(leave != null ? leave.getCarriedOverDay() : 0);

        return userResponse;
    }


    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(changePasswordRequest.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Account account =user.getAccount();

        if(passwordEncoder.matches(changePasswordRequest.getOldPass(), account.getPassword())){

            account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPass()));
        }else {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    @Override
    public User getUserToUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(user.getStatus().equals(UserStatusEnum.TERMINATED)) throw new AppException(ErrorCode.USER_TERMINATED);

        return user;
    }

    private List<NotificationRecipientResponse> getTop5NotificationRecipient(long userId) {
        Pageable pageable = PageRequest.of(0,5);
        List<NotificationRecipient> notificationRecipients = notificationRecipientRepository.findTop5ByUserId(userId, pageable);

        return notificationRecipients.stream().map(notificationRecipient -> {
            return NotificationRecipientResponse.builder()
                    .id(notificationRecipient.getId())
                    .title(notificationRecipient.getNotification().getTitle())
                    .readStatus(notificationRecipient.isReadStatus())
                    .sendDate(notificationRecipient.getNotification().getSendDate()).build();
        }).toList();
    }
    private String createSendPWMessage(String username, String password, String name){
        return String.format(
                "<html>" +
                        "<body>" +
                        "<p>Xin chào <b>%s</b>,</p>" +
                        "<p>Tài khoản của bạn đã được tạo thành công.</p>" +
                        "<p><strong>Thông tin đăng nhập:</strong></p>" +
                        "<ul>" +
                        "<li><b>Tên đăng nhập:</b> %s</li>" +
                        "<li><b>Mật khẩu:</b> %s</li>" +
                        "</ul>" +
                        "<p style='color:red;'><strong>Vui lòng đổi mật khẩu ngay sau khi đăng nhập để bảo vệ tài khoản của bạn.</strong></p>" +
                        "</body>" +
                        "</html>",
                name, username, password
        );
    }
}
