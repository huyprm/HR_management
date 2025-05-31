package org.ptithcm2021.hr_management.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.dto.request.ChangePasswordRequest;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.request.UserUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.NotificationRecipientResponse;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.dto.response.WorkLogResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.UserMapper;
import org.ptithcm2021.hr_management.mapper.WorkLogMapper;
import org.ptithcm2021.hr_management.model.*;
import org.ptithcm2021.hr_management.projection.UserSummary;
import org.ptithcm2021.hr_management.repository.*;
import org.ptithcm2021.hr_management.service.MailService;
import org.ptithcm2021.hr_management.service.UserService;
import org.ptithcm2021.hr_management.util.LeaveApplicationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final NotificationRecipientRepository notificationRecipientRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final WorkLogRepository workLogRepository;
    private final WorkLogMapper workLogMapper;
    private final LeaveApplicationUtil leaveApplicationUtil;

    @Override
    public UserResponse createUser(UserRequest userRequest) throws MessagingException {
        if (userRepository.existsUserByEmail(userRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = userMapper.toUser(userRequest);

        Random random = new Random();
        String password = String.format("%08d", random.nextInt(100_000_000));

        Account account = Account.builder()
                .username(user.getEmail())
                .password(passwordEncoder.encode(password))
                .build();

        user.setAccount(account);

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
        //userResponse.setRecipientResponse(getTop5NotificationRecipient(id));

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
        //userResponse.setRecipientResponse(getTop5NotificationRecipient(userId));

        //Seniority Info
        SeniorityAllowanceRule rule;
        if( (rule = user.getSeniorityAllowanceRule()) != null){
            userResponse.setSeniorityPercentage(rule.getSeniorityPercentage());
            userResponse.setSeniorityLeaveDay(rule.getSeniorityLeaveDay());
        }

        List<Object[]> decisionCounts = workLogRepository.countRewardAndDisciplineByUserId(userId);

        Map<String, Integer> decisionCountMap = decisionCounts.stream().collect(Collectors.toMap(o -> String.valueOf(o[0]),  o -> ((Number) o[1]).intValue()));
        userResponse.setNumReward(decisionCountMap.getOrDefault("AWARD", 0));
        userResponse.setNumDiscipline(decisionCountMap.getOrDefault("DISCIPLINE", 0));

        // Leave Balance
        YearMonth currentMonth = YearMonth.now();
        YearMonth preMonth = YearMonth.now();

        LocalDate endDate = currentMonth.atEndOfMonth();
        LocalDate startDate = currentMonth.atDay(1);

        LeaveBalance leave = leaveBalanceRepository.findByUserIdAndYearAndMonth
                (userId, preMonth.getYear(), preMonth.getMonthValue()).orElse(null);

        userResponse.setCarriedOverDay(leave != null ? leave.getRemainingLeaveDay() : 0);

        double numLeave = leaveApplicationUtil.calculateTotalLeveDays(userId, startDate, endDate);

        userResponse.setUsedLeaveDay(numLeave);
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
    public PagedModel<UserResponse> getAllUserByStatus(UserStatusEnum status, Pageable pageable) {
        Page<UserResponse> userResponses;
        if (status == null) {
            userResponses = userRepository.findAll(pageable).map(userMapper::toUserResponse);
            return new PagedModel<>(userResponses);
        }
        return new PagedModel<>(userRepository.findAllByStatus(status, pageable).map(userMapper::toUserResponse));
    }

    @Override
    public User getUserToUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(user.getStatus().equals(UserStatusEnum.TERMINATED)) throw new AppException(ErrorCode.USER_TERMINATED);

        return user;
    }

    @Override
    public List<WorkLogResponse> getWorkLogByUserId(long userId) {
        return workLogRepository
                .findAllByUserId(userId)
                .stream()
                .map(workLogMapper::toWorkLogResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PagedModel<UserResponse> getAllUserByRole(RoleEnum roleName, Pageable pageable) {
        return new PagedModel<>(userRepository.findAllUserByRole(roleName, pageable)
                .map(userMapper::toUserResponse));
    }

    @Override
    public PagedModel<UserResponse> getAllUserByContract(ContractStatusEnum contractStatusEnum, Pageable pageable) {
        if(contractStatusEnum == null)
            return new PagedModel<>(userRepository.findUsersWithoutContract(pageable).map(userMapper::toUserResponse));

        return new PagedModel<>(userRepository.findAllUserByContract(contractStatusEnum, pageable)
                .map(userMapper::toUserResponse));
    }

    @Override
    public List<UserSummary> searchUser(String keyword) {

        String[] handleKeyword = keyword.split(" ");
        StringBuilder finalKey= new StringBuilder();
        for (String handle : handleKeyword){
            finalKey.append('+').append(handle.toLowerCase()).append(" ");
        }
        finalKey.append('*');
        log.info(finalKey.toString());
        return userRepository.searchFullText(finalKey.toString());
    }

    @Override
    public String saveDeviceToken(long userId, String deviceToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.getAccount().setDeviceToken(deviceToken);
        userRepository.save(user);
        return "Saved device token";
    }

    @Override
    public PagedModel<UserResponse> getAllUserByDepartment(String departmentId, UserStatusEnum status, Pageable pageable) {
        Page<UserResponse> users;

        if(status == null ){
            users = userRepository.findAllByDepartmentId(departmentId, pageable)
                    .map(userMapper::toUserResponse);
        } else {
            users = userRepository.findByDepartmentIdAndStatus(departmentId, status, pageable)
                    .map(userMapper::toUserResponse);
        }

        return new PagedModel<>(users);
    }

    @Override
    public void removeDeviceToken(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.getAccount().setDeviceToken(null);
        userRepository.save(user);
    }

    private Page<NotificationRecipientResponse> getTop5NotificationRecipient(long userId) {
        Pageable pageable = PageRequest.of(0,5);
        Page<NotificationRecipient> notificationRecipients = notificationRecipientRepository.findAllByUserId(userId, pageable);

        return notificationRecipients.map(notificationRecipient -> {
            return NotificationRecipientResponse.builder()
                    .id(notificationRecipient.getId())
                    .title(notificationRecipient.getNotification().getTitle())
                    .readStatus(notificationRecipient.isReadStatus())
                    .sendDate(notificationRecipient.getNotification().getSendDate()).build();
        });
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
